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

  /**
   * play the turn. the method contains all the logic to play a turn, and it should be able to
   * effect the player's hand and the table itself
   * @param handler the action handler for making commands to the table
   */
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
