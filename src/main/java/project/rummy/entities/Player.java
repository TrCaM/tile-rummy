package project.rummy.entities;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;

public class Player {
  private Hand hand;
  private PlayerStatus status;
  private Controller controller;
  private String name;
  private int id;

  public Player(String name, Controller controller, int id) {
    this.name = name;
    this.hand = new Hand();
    this.status = PlayerStatus.START;
    this.controller = controller.setPlayer(this);
    this.id = id;
  }

  public Player(String name, Controller controller, Hand hand, PlayerStatus status, int id) {
    this.name = name;
    this.hand = hand;
    this.status = status;
    this.id = id;
    this.controller = controller.setPlayer(this);
  }

  public void resetForNewTurn() {
    hand.getTiles().forEach(tile -> tile.setHightlight(false));
  }
  public Controller getController() {
    return this.controller;
  }

  public void setHand(Hand hand) {
    this.hand = hand;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public PlayerData toPlayerData(){
    return new PlayerData(name, controller.getControllerType());
  }
}
