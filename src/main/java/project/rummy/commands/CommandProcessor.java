package project.rummy.commands;

import project.rummy.control.ActionHandler;

public class CommandProcessor {
  private ActionHandler handler;

  public CommandProcessor() {
    handler = null;
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
    if (handler == null || handler.isExpired()) {
      throw new IllegalStateException("ActionHandler was not set up properly before the turn");
    }
    command.execute(handler);
  }
}
