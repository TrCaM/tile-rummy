package project.rummy.strategies;

import project.rummy.control.ActionHandler;

public interface Strategy {
  /**
   * Play the first 30+ point turn
   * @param handler the action handler to communicate with the board and hand during the turn
   */
  void iceBreak(ActionHandler handler);


  /**
   * Play the turn after the first 30+ turn. Note that at this point the player can manipulate the
   * table
   * @param handler the action handler to communicate with the board and hand during the turn
   */
  void performFullTurn(ActionHandler handler);
}
