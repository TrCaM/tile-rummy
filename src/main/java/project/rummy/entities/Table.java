package project.rummy.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represent the table entity of the game
 */
public class Table {
  private List<Tile> freeTiles;
  private List<Meld> melds;
  /**
   * Note for back up melds: It is used for restoring the table before each turn.
   */
  private List<Meld> backupMelds;

  static final int MAX_VALUE = 13;
  static final int DECKS_AMOUNT = 2;

  public Table() {
    this.melds = new ArrayList<>();
    this.backupMelds = new ArrayList<>();
    this.freeTiles = new ArrayList<>();
  }

  public void initTiles() {
    createDeck();
  }

  public void shuffle() {
    Collections.shuffle(freeTiles);
  }

  private void createDeck() {
    for (int j = 0; j < DECKS_AMOUNT; j++) {
      for (Color color : Color.values()) {
        for (int i = 1; i <= MAX_VALUE; i++) {
          this.freeTiles.add(Tile.createTile(color, i));
        }
      }
    }
  }

  /**
   * Storing the current list of melds so that we can restore the table as it was at the beginning
   * of each turn.
   */
  public void backupMelds() {
    for (Meld meld : melds) {
      Tile[] tiles = new Tile[meld.tiles().size()];
      meld.tiles().toArray(tiles);
      backupMelds.add(Meld.createMeld(tiles));
    }
  }

  /**
   * Restore the state of the table as in the backup melds.
   */
  public void restoreFromBackup() {
    this.melds = backupMelds;
  }


  public List<Tile> getFreeTiles() {
    return this.freeTiles;
  }

  public List<Meld> getPlayingMelds() {
    return this.melds;
  }

  public List<Meld> getBackupMelds() {
    return this.backupMelds;
  }
  /**
   * Add meld into the table if the input meld is a valid meld and return true. Otherwise not add
   * and return false.
   */
  public boolean addMeld(Meld meld) {
    if (!meld.isValidMeld()) {
      return false;
    }
    melds.add(meld);
    return true;
  }

  public Meld removeMeld(int index) {
    return melds.remove(index);
  }

  public Tile drawTile() {
    return freeTiles.remove(freeTiles.size() - 1);
  }
}
