package project.rummy.control;

import project.rummy.commands.Command;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.Player;

public abstract class Controller {
  protected Player player;
  private CommandProcessor processor;

  protected Controller() {
    processor = CommandProcessor.getInstance();
  }

  /**
   * play the turn. the method contains all the logic to play a turn, and it should be able to
   * effect the player's hand and the table itself
   * @param handler the action handler for making commands to the table
   */
  public abstract void playTurn();

  protected void send(Command command) {
    processor.apply(command);
  }

  public Controller setPlayer(Player player) {
    this.player = player;
    return this;
  }
}
