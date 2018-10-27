package project.rummy.commands;

import project.rummy.control.ActionHandler;

/**
 * Command provide an interface so that each player can communicate with the game system. This
 * will help the integration with the UI become a bit easier as we can combine a lot of moves in
 * {@link project.rummy.control.ActionHandler} in one single command.
 */
public interface Command {
  void execute(ActionHandler handler);
}
