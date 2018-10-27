package project.rummy.control;

import project.rummy.entities.Player;

public abstract class Controller {
  protected Player player;

  /**
   * play the turn. the method contains all the logic to play a turn, and it should be able to
   * effect the player's hand and the table itself
   * @param handler the action handler for making commands to the table
   */
  public abstract void playTurn();

  public Controller setPlayer(Player player) {
    this.player = player;
    return this;
  }
}
