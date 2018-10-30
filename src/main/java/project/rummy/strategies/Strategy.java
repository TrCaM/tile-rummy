package project.rummy.strategies;

import project.rummy.commands.Command;

import java.util.List;

public interface Strategy {
  /**
   * Play the first 30+ point turn
   */
  List<Command> iceBreak();


  /**
   * Play the turn after the first 30+ turn. Note that at this point the controllers can manipulate the
   * table
   */
  List<Command> performFullTurn();
}
