package project.rummy.commands;

import project.rummy.control.ActionHandler;

public class BreakCommand implements Command {


    @Override
    public void execute(ActionHandler handler) {
        handler.forceUpdate();
    }
}
