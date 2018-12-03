package project.rummy.strategies;

import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

public class Strategy4 extends Strategy3 {
  private Strategy fastStrategy;
  private Strategy slowStrategy;

  public Strategy4() {
    fastStrategy = new Strategy1();
    slowStrategy = new StrategyAnalyzingTable(true);
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    this.turn = state.getTurnNumber();
    return fastStrategy.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    return  getPlayFullTurnStrategy(gameState, fastStrategy, slowStrategy).performFullTurn(gameState);
  }
}
