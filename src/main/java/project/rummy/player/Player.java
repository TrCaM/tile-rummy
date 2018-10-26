package project.rummy.player;

import project.rummy.control.ActionHandler;
import project.rummy.control.PlayerStatus;
import project.rummy.entities.Hand;

public abstract class Player {
  protected Hand hand;
  protected PlayerStatus status;

  public Player() {
    this.hand = new Hand();
    this.status = PlayerStatus.START;
  }

  public abstract void playTurn(ActionHandler handler);

  public void setStatus(PlayerStatus status) {
    this.status = status;
  }

  public Hand getHand() {
    return hand;
  }

  public PlayerStatus getStatus() {
    return status;
  }
}
