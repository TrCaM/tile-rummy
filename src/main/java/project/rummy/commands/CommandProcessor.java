package project.rummy.commands;

import project.rummy.control.ActionHandler;
import project.rummy.game.Game;

public class CommandProcessor {
  private Game game;
  private ActionHandler handler;

  private static final CommandProcessor INSTANCE = new CommandProcessor();

  public static CommandProcessor getInstance() {
    return INSTANCE;
  }


  private CommandProcessor() {
    game = null;
    handler = null;
  }

  /**
   * Set up the {@link Game} for processing the commands.
   */
  public void setUpGame(Game game) {
    this.game = game;
  }

  /**
   * Set up the {@link ActionHandler} for processing the commands.
   */
  public void setUpHandler(ActionHandler handler) {
    this.handler =  handler;
  }


  /**
   * Execute the command. Throw if the action handler used is expired. This is because we want every
   * handler to be used only in one turn.
   */
  public void apply(Command command) {
    if (game == null) {
      throw new IllegalStateException("Game was not set up properly");
    }
    if (handler == null || handler.isExpired()) {
      throw new IllegalStateException("ActionHandler was not set up properly before the turn");
    }
    command.execute(handler);
    game.notifyObservers();
  }
}
