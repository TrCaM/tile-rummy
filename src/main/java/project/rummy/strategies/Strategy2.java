package project.rummy.strategies;

import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

public class Strategy2 implements Strategy {
  private Strategy behindStrat;

  public Strategy2() {
    this.behindStrat = new StrategyAnalyzingTable(false);
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return behindStrat.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState state) {
    return behindStrat.performFullTurn(state);
  }
}
