package project.rummy.strategies;

import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.List;


/**
 *  Strategy 3 as the specification.
 */
public class Strategy3 implements Strategy, Observer {
  private GameState state;

  public Strategy3(Game game) {
    game.registerObserver(this);
  }

  @Override
  public PlayDirection iceBreak() {
    return null;
  }

  @Override
  public PlayDirection performFullTurn() {
    return null;
  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }
}

