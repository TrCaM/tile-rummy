package project.rummy.behaviors;

import project.rummy.commands.Command;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class FastTilesOnlyMoveMaker implements ComputerMoveMaker {
  @Override
public List<Command> calculateMove(GameState state) {
  List<Command> commands = new ArrayList<>();

  ComputerMoveMaker move = new PlayOneTileMoveMaker();

  List<Command> receivedCommands = move.calculateMove(state);
  while(!receivedCommands.isEmpty()){
    commands.addAll(receivedCommands);
    receivedCommands = move.calculateMove(state);
  }
  return commands;
}
}
