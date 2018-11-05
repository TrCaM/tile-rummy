package project.rummy.strategies;

import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

import java.util.stream.IntStream;

public class Strategy4 implements Strategy {
  private Strategy fastStrategy;
  private Strategy slowStrategy;
  private Strategy activeStrategy;
  private int turn;

  public Strategy4() {
    fastStrategy = new Strategy1();
    activeStrategy = fastStrategy;
    slowStrategy = new StrategyAnalyzingTable(true);
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    this.turn = state.getTurnNumber();
    return fastStrategy.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    if (gameState.getTurnNumber() > turn) {
      activeStrategy = shouldPlayAggressive(gameState) ? fastStrategy : slowStrategy;
      this.turn = gameState.getTurnNumber();
    }
    return  activeStrategy.performFullTurn(gameState);
  }

  private boolean shouldPlayAggressive(GameState gameState) {
    int currentHandSize = gameState.getHandsData()[gameState.getCurrentPlayer()].tiles.size();
    return IntStream.range(0, 4)
        .filter(num -> currentHandSize - gameState.getHandsData()[num].tiles.size() > 3)
        .findAny().isPresent();
  }
}
