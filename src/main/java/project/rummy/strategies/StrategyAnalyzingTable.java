package project.rummy.strategies;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.behaviors.*;
import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.control.ActionHandler;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class StrategyAnalyzingTable implements Strategy {
  private ComputerMoveMaker iceBreaking;
  private ComputerMoveMaker playAllMelds;
  private ComputerMoveMaker playOneTile;

  StrategyAnalyzingTable(boolean shouldAnalyzeTable) {

    this.iceBreaking = shouldAnalyzeTable ? new FastIceBreakingMoveMaker() : new SlowIceBreakingMoveMaker();
    this.playAllMelds = new PlayAllMeldsMoveMaker();
    this.playOneTile = new PlayOneTileMoveMaker(shouldAnalyzeTable);
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return new PlayDirection(iceBreaking.calculateMove(state));
  }

  @Override
  public PlayDirection performFullTurn(GameState state) {
    List<Command> pushCommands = new ArrayList<>();
    if (state.getHandsData()[state.getCurrentPlayer()].tiles.size() == 0) {
      pushCommands.add(ActionHandler::endTurn);
      return new PlayDirection(pushCommands);
    }
    //find list of tiles that cannot form melds
    List<Tile> handTiles = new ArrayList<>(state.getHandsData()[state.getCurrentPlayer()].tiles);
    List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);
    allMelds.forEach(meld -> meld.tiles().forEach(handTiles::remove));
    if (handTiles.isEmpty() && !allMelds.isEmpty()) {
      pushCommands.addAll(playAllMelds.calculateMove(state));
    } else {
      pushCommands.addAll(playOneTile.calculateMove(state));
    }

    if (pushCommands.isEmpty()) {
      pushCommands.add(ActionHandler::drawOrEndTurn);
    }

    return new PlayDirection(pushCommands);
  }
}
