package project.rummy.control;

import project.rummy.entities.PlayerStatus;
import project.rummy.strategies.Strategy;

public class AutoController extends Controller {
  private Strategy strategy;

  public AutoController(Strategy strategy) {
    super();
    this.strategy = strategy;
  }

  @Override
  public void playTurn() {
    if (player.status() == PlayerStatus.START) {
      strategy.iceBreak();
    } else {
      strategy.performFullTurn();
    }
  }
}
