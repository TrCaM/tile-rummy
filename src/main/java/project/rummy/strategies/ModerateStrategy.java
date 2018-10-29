package project.rummy.strategies;

import project.rummy.control.ActionHandler;

/**
 * Moderate Strategy (or strategy 3 in the specification). Specifically, computer controllers using
 * this strategy will:
 *  + Play its ice break turn as soon as possible (like controllers 1)
 *  + During its turns after the ice break turn:
 *    + if any of other controllers has less than 3 tiles than this controllers then try to play anything it
 *      could
 *    + otherwise, play like passive strategy (play tiles that requires table)
 */
public class ModerateStrategy implements  Strategy{
  @Override
  public void iceBreak() {
    
  }

  @Override
  public void performFullTurn() {

  }
}
