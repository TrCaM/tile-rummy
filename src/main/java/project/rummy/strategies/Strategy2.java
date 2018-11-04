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

/**
 * Strategy 2 as the specifiation.
 */
public class Strategy2 implements Strategy {
  private ComputerMoveMaker iceBreaking;
  private ComputerMoveMaker playAllMelds;
  private ComputerMoveMaker playOneTile;

  public Strategy2() {
    this.iceBreaking = new SlowIceBreakingMoveMaker();
    this.playAllMelds = new PlayAllMeldsMoveMaker();
    this.playOneTile = new PlayOneTileMoveMaker();
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return new PlayDirection(iceBreaking.calculateMove(state));
  }

  @Override
  public PlayDirection performFullTurn(GameState state) {
    //find list of tiles that cannot form melds
    List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;
    List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);
    allMelds.forEach(meld -> meld.tiles().forEach(handTiles::remove));
    List<Command> pushCommands = new ArrayList<>();
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
