package project.rummy.strategies;

import project.rummy.commands.Command;
import project.rummy.commands.CommandChunks;
import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

import java.util.List;

public interface Strategy {
  /**
   * Play the first 30+ point turn
   */
  PlayDirection iceBreak(GameState gameState);

  /**
   * Play the turn after the first 30+ turn. Note that at this point the controllers can manipulate the
   * table
   */
  PlayDirection performFullTurn(GameState gameState);
}
