package project.rummy.strategies;

import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

public class Strategy2 implements Strategy {
  private Strategy behindStrategy;

  public Strategy2() {
    this.behindStrategy = new StrategyAnalyzingTable(false);
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return behindStrategy.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState state) {
    return behindStrategy.performFullTurn(state);
  }
}
