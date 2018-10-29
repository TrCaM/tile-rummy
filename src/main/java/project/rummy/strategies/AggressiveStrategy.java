package project.rummy.strategies;

import project.rummy.control.ActionHandler;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

/**
 * Aggressive Strategy (or strategy 1 in the specification). Specifically, computer controllers using
 * this strategy why try to play anything that he has in his hand and try to manipulate the table
 * to play as much as possible
 */
public class AggressiveStrategy implements Strategy, Observer {
  private GameState state;

  public AggressiveStrategy(Game game) {
    game.registerObserver(this);
  }

  @Override
  public void iceBreak() {

  }

  @Override
  public void performFullTurn() {

  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }
}
