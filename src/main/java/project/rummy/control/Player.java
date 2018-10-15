package project.rummy.control;

import project.rummy.entities.Hand;

public class Player {
  private Hand hand;
  private PlayerStatus status;

  public Player() {
    this.hand = new Hand();
    this.status = PlayerStatus.START;
  }

  public  void setStatus(PlayerStatus status) {
    this.status = status;
  }

  public Hand getHand() {
    return hand;
  }

  public PlayerStatus getStatus() {
    return status;
  }
}
