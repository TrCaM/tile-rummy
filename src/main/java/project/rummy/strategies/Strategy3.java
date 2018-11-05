package project.rummy.strategies;

import project.rummy.behaviors.ComputerMoveMaker;
import project.rummy.behaviors.FastIceBreakingMoveMaker;
import project.rummy.behaviors.PlayAllMeldsMoveMaker;
import project.rummy.behaviors.PlayOneTileMoveMaker;
import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.control.ActionHandler;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 *  Strategy 3 as the specification.
 */
public class Strategy3 implements Strategy {
  private Strategy strategy1;
  private Strategy strategy2;

  public Strategy3() {
    strategy1 = new Strategy1();
    strategy2 = new Strategy2();
  }

  @Override
  public PlayDirection iceBreak(GameState state) {
    return strategy1.iceBreak(state);
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    return shouldPlayAggressive(gameState)
        ? strategy1.performFullTurn(gameState)
        : strategy2.performFullTurn(gameState);
  }

  private boolean shouldPlayAggressive(GameState gameState) {
    int currentHandSize = gameState.getHandsData()[gameState.getCurrentPlayer()].tiles.size();
    return IntStream.range(0, 4)
        .filter(num -> currentHandSize - gameState.getHandsData()[num].tiles.size() > 3)
        .findAny().isPresent();
  }
}

