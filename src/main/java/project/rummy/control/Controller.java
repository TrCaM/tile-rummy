package project.rummy.control;

import project.rummy.commands.Command;
import project.rummy.commands.CommandChunks;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.Player;

import java.util.List;

public abstract class Controller {
  protected Player player;
  private CommandProcessor processor;

  protected Controller() {
    processor = CommandProcessor.getInstance();
  }

  public abstract String getControllerType();

  /**
   * play the turn. the method contains all the logic to play a turn, and it should be able to
   * effect the player's hand and the table itself
   */
  public abstract void playTurn();

  public abstract void endTurn();

  public abstract void closeInput();

  protected void send(List<Command> commands, List<CommandChunks> chunksList) {
    commands.forEach(processor::enqueueCommand);
    chunksList.forEach(processor::enqueueChunks);
  }

  protected void send(Command command) {
    processor.enqueueCommand(command);
  }
  public Controller setPlayer(Player player) {
    this.player = player;
    return this;
  }
}
