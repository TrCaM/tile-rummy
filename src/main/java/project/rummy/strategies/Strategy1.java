package project.rummy.strategies;

import project.rummy.behaviors.*;
import project.rummy.commands.Command;
import project.rummy.commands.CommandChunks;
import project.rummy.commands.PlayDirection;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;


/**
 * Aggressive Strategy (or strategy 1 in the specification). Specifically, computer controllers using
 * this strategy why try to play anything that he has in his hand and try to manipulate the table
 * to play as much as possible
 */
public class Strategy1 implements Strategy, Observer {
  private GameState state;
  public Strategy1(Game game) {
    game.registerObserver(this);
  }

  @Override
  public PlayDirection iceBreak() {
    ComputerMoveMaker move = new FastIceBreakingMoveMaker();
    return new PlayDirection(move.calculateMove(state));
  }

  @Override
  public PlayDirection performFullTurn() {
    PlayDirection playDirection = new PlayDirection();

    ComputerMoveMaker playMeld = new PlayAllMeldsMoveMaker();
    ComputerMoveMaker playTile = new PlayOneTileMoveMaker();

    playDirection.addDirection((gameState, handler) -> playMeld.calculateMove(gameState));
    playDirection.addDirection((gameState, handler) -> playTile.calculateMove(gameState));
    playDirection.addDirection((gameState, handler)-> {
      List<Command> commands = new ArrayList<>();
      commands.add(handle -> {
        if (handle.getTurnStatus().canDraw) {
          handle.draw();
        }
      });
      return commands;
    });

    return playDirection;
  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }
}
