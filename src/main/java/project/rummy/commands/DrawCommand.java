package project.rummy.commands;

import project.rummy.control.ActionHandler;

public class DrawCommand implements Command{

  @Override
  public void execute(ActionHandler handler) {
    handler.drawAndEndTurn();
  }
}
