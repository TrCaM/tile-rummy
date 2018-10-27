package project.rummy.commands;

import project.rummy.control.ActionHandler;

public class PlayMeldCommand implements Command{
  private int index;

  public PlayMeldCommand(int index) {
    this.index = index;
  }

  @Override
  public void execute(ActionHandler handler) {
    handler.playFromHand(index);
  }
}
