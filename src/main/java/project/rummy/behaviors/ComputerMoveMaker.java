package project.rummy.behaviors;

import project.rummy.commands.Command;
import project.rummy.game.GameState;

import java.util.List;

public interface ComputerMoveMaker {
  List<Command> calculateMove(GameState state);
}
