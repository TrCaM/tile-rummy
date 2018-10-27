package project.rummy.entities;

import project.rummy.control.Controller;

public class Player {
  private Hand hand;
  private PlayerStatus status;
  private Controller controller;

  public Player(Controller controller) {
    this.hand = new Hand();
    this.status = PlayerStatus.START;
    this.controller = controller.setPlayer(this);
  }

  public Controller getController() {
    return this.controller;
  }

  public Hand hand() {
    return hand;
  }

  public PlayerStatus status() {
    return status;
  }

  public void setStatus(PlayerStatus status) {
    this.status = status;
  }
}
