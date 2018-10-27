package project.rummy.strategies;

import project.rummy.control.ActionHandler;

/**
 * Passive Strategy (or strategy 2 in the specification). Specifically, computer player using
 * this strategy will:
 *  + only play ice break turn as long as another player has played its ice break turn
 *  + unless being able to play all this tiles to win, play tiles that require manipulate meld from
 *    the table
 */
public class PassiveStrategy implements Strategy{

  @Override
  public void iceBreak(ActionHandler handler) {

  }

  @Override
  public void performFullTurn(ActionHandler handler) {

  }
}
