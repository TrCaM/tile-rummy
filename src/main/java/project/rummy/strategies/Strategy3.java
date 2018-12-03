package project.rummy.strategies;

import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;
import java.util.stream.IntStream;


/**
 *  Strategy 3 as the specification.
 */
public class Strategy3 implements Strategy {
  private Strategy strategy1;
  private Strategy strategy2;
  protected int turn;

  public Strategy3() {
    strategy1 = new Strategy1();
    strategy2 = new Strategy2();
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    this.turn = state.getTurnNumber();
    return strategy1.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    return  getPlayFullTurnStrategy(gameState, strategy1, strategy2).performFullTurn(gameState);
  }

  Strategy getPlayFullTurnStrategy(GameState gameState, Strategy strategy1, Strategy strategy2) {
    Strategy activeStrategy = strategy1;
    if (gameState.getTurnNumber() > turn) {
      activeStrategy = shouldPlayAggressive(gameState) ? strategy1 : strategy2;
      this.turn = gameState.getTurnNumber();
    }
    return activeStrategy;
  }

  private boolean shouldPlayAggressive(GameState gameState) {
    int currentHandSize = gameState.getHandsData()[gameState.getCurrentPlayer()].tiles.size();
    return IntStream.range(0, 4)
        .filter(num -> currentHandSize - gameState.getHandsData()[num].tiles.size() > 3)
        .findAny().isPresent();
  }
}
