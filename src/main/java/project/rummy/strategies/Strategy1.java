package project.rummy.strategies;

import project.rummy.behaviors.*;
import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.control.ActionHandler;
import project.rummy.game.GameState;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggressive Strategy (or strategy 1 in the specification). Specifically, computer controllers using
 * this strategy why try to play anything that he has in his hand and try to manipulate the table
 * to play as much as possible
 */
public class Strategy1 implements Strategy {
  private ComputerMoveMaker iceBreakingMoveMaker;
  private ComputerMoveMaker playAllMeldsMoveMaker;
  private ComputerMoveMaker playOneTileMoveMaker;

  public Strategy1() {
    this.iceBreakingMoveMaker = new FastIceBreakingMoveMaker();
    this.playAllMeldsMoveMaker = new PlayAllMeldsMoveMaker();
    this.playOneTileMoveMaker = new PlayOneTileMoveMaker();
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return new PlayDirection(iceBreakingMoveMaker.calculateMove(state));
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    List<Command> pushCommands = new ArrayList<>(playAllMeldsMoveMaker.calculateMove(gameState));
    if (pushCommands.isEmpty()) {
      pushCommands.addAll(playOneTileMoveMaker.calculateMove(gameState));
    }
    if (pushCommands.isEmpty()) {
      pushCommands.add(ActionHandler::drawOrEndTurn);
    }
    return new PlayDirection(pushCommands);
  }
}
