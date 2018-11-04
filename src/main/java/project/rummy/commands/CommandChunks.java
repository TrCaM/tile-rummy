package project.rummy.commands;

import project.rummy.control.ActionHandler;
import project.rummy.game.GameState;

import java.util.List;

public interface CommandChunks {
    List<Command> execute(GameState state, ActionHandler handler);
}
