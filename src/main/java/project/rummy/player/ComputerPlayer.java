package project.rummy.player;

import project.rummy.control.ActionHandler;
import project.rummy.control.PlayerStatus;
import project.rummy.strategies.Strategy;

public class ComputerPlayer extends Player{
  private Strategy strategy;

  public ComputerPlayer(Strategy strategy) {
    super();
    this.strategy = strategy;
  }

  @Override
  public void playTurn(ActionHandler handler) {
    if (status == PlayerStatus.START) {
      strategy.iceBreak(handler);
    } else {
      strategy.performFullTurn(handler);
    }
  }
}
