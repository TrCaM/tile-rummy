package project.rummy.control;

import org.apache.log4j.Logger;
import project.rummy.entities.*;

/**
 * This class handles all controllers's interaction with the game.
 */
public class ActionHandler {
  private Hand hand;
  private boolean canUseTable;
  private Table table;
  private ManipulationTable manipulationTable;
  private boolean isTurnEnd;
  private String playerName;

  private static Logger logger = Logger.getLogger(ActionHandler.class);

  public ActionHandler(Player player, Table table) {
    this.hand = player.hand();
    this.canUseTable = player.status() != PlayerStatus.START;
    this.table = table;
    this.manipulationTable = ManipulationTable.getInstance();
    manipulationTable.clear();
    this.isTurnEnd = false;
    this.playerName = player.getName();
  }

  public Hand getHand(){
    return this.hand;
  }

  public void formMeld(int ...indexes){
    hand.formMeld(indexes);
  }

  public boolean isExpired() {
    return this.isTurnEnd;
  }

  public void draw() {
    Tile tile = table.drawTile();
    hand.addTile(tile);
    logger.info(String.format("%s has draw %s", playerName, tile));
  }

  public void playFromHand(int meldIndex) {
    //TODO: Add a logging infomation here
    if (meldIndex >=0 && meldIndex < hand.getMelds().size()) {
      manipulationTable.add(hand.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }

  public void playFromHand(Meld meld) {
    //TODO: Add a logging infomation here
    playFromHand(hand.getMelds().indexOf(meld));
  }

  public void takeTableMeld(int meldIndex){
    //TODO: Add a logging infomation here
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (meldIndex >=0 && meldIndex < table.getPlayingMelds().size()) {
      manipulationTable.add(table.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }
  public void takeHandTile(int tileIndex){
    //TODO: Add a logging infomation here
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (tileIndex >=0 && tileIndex < hand.getTiles().size()) {
      manipulationTable.add(Meld.createMeld(hand.removeTile(tileIndex)));
    } else {
      throw new IllegalArgumentException("Invalid tile index");
    }
  }

  public boolean endTurn() {
    // TODO: Add logging information here
    isTurnEnd = manipulationTable.submit(table);
    return isTurnEnd;
  }

  public ManipulationTable getManipulationTable() {
    return this.manipulationTable;
  }
}
