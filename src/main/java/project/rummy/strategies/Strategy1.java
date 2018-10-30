package project.rummy.strategies;

import project.rummy.commands.Command;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

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
  public List<Command> iceBreak() {
    return null;
  }

  @Override
  public List<Command> performFullTurn() {
    return null;
  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }
}
