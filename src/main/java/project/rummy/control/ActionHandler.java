package project.rummy.control;

import project.rummy.entities.*;

/**
 * This class handles all controllers's interaction with the game.
 */
public class ActionHandler {
  private Hand hand;
  private boolean canUseTable;
  private Table table;
  private ManipulationTable manipulationTable;

  public ActionHandler(Player player, Table table) {
    this.hand = player.hand();
    this.canUseTable = player.status() != PlayerStatus.START;
    this.table = table;
    this.manipulationTable = ManipulationTable.getInstance();
    manipulationTable.clear();
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

  public void playFromHand(Meld meld) {
    playFromHand(hand.getMelds().indexOf(meld));
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
