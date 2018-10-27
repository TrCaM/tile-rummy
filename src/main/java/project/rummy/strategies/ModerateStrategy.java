package project.rummy.strategies;

import project.rummy.control.ActionHandler;

/**
 * Moderate Strategy (or strategy 3 in the specification). Specifically, computer player using
 * this strategy will:
 *  + Play its ice break turn as soon as possible (like player 1)
 *  + During its turns after the ice break turn:
 *    + if any of other player has less than 3 tiles than this player then try to play anything it
 *      could
 *    + otherwise, play like passive strategy (play tiles that requires table)
 */
public class ModerateStrategy implements  Strategy{
  @Override
  public void iceBreak(ActionHandler handler) {
    
  }

  @Override
  public void performFullTurn(ActionHandler handler) {

  }
}
