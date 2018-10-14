package project.rummy.control;

import project.rummy.entities.ManipulationTable;
import project.rummy.entities.Meld;
import project.rummy.entities.Table;

/**
 * This class handles all player's interaction with the game.
 */
public class ActionHandler {
  private Player player;
  private Table copiedTable;
  private ManipulationTable manipulationTable;

  public ActionHandler(Player player, Table table) {
    this.player = player;
  //  this.copiedTable = table.copy();
    this.manipulationTable = new ManipulationTable();
  }

  public void draw() {
    // TODO: write tests and implement
    throw new UnsupportedOperationException();
  }

  public void playFromHand(Meld meld) {
    // TODO: write tests and implement, remember to remove tiles from hand and add to manipulation table.
    throw new UnsupportedOperationException();
  }

  public void manipulateMeld(int meldIndex) {
    // TODO: write tests and implement, add meld from table to the manipulation table.
    throw new UnsupportedOperationException();
  }

  public void endTurn() {
    // TODO: write tests and implement
    throw new UnsupportedOperationException();
  }

  public ManipulationTable getManipulationTable() {
    return this.manipulationTable;
  }
}
