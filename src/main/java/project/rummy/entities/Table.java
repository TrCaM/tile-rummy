package project.rummy.entities;

import java.util.List;

/**
 * This class represent the table entity of the game
 */
public class Table {
  private List<Tile> freeTiles;
  private List<Meld> melds;

  /**
   * Generate all the tiles needed for the game and shuffle them randomly
   * TODO: Write tests and implement
   */
  public void initTiles() {
    throw new UnsupportedOperationException();
  }

  public void addMeld(Meld meld) {
    melds.add(meld);
  }

  public Meld removeMeld(int index) {
    return melds.remove(index);
  }

  public Tile drawTile() {
    return freeTiles.remove(freeTiles.size() - 1);
  }
}
