package project.rummy.commands;

import project.rummy.control.ActionHandler;

public interface Command {
  void execute(ActionHandler handler);
}
