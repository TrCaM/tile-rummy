package project.rummy.control;

import project.rummy.entities.Hand;
import project.rummy.entities.ManipulationTable;
import project.rummy.entities.Table;

/**
 * This class handles all player's interaction with the game.
 */
public class ActionHandler {
  private Hand hand;
  private boolean canUseTable;
  private Table table;
  private ManipulationTable manipulationTable;

  public ActionHandler(Player player, Table table) {
    this.hand = player.getHand();
    this.canUseTable = player.getStatus() != PlayerStatus.START;
    this.table = table;
    this.manipulationTable = new ManipulationTable();
  }

  public void draw() {
    hand.addTile(table.drawTile());
  }

  public void playFromHand(int meldIndex) {
    if (meldIndex >=0 && meldIndex < hand.getMelds().size()) {
      manipulationTable.add(hand.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }

  public void takeTableMeld(int meldIndex) throws IllegalAccessException {
    if (!canUseTable) {
      throw new IllegalAccessException("Cannot manipulate table");
    }
    if (meldIndex >=0 && meldIndex < table.getPlayingMelds().size()) {
      manipulationTable.add(table.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }

  public boolean endTurn() {
    return manipulationTable.submit(table);
  }

  public ManipulationTable getManipulationTable() {
    return this.manipulationTable;
  }
}
