package project.rummy.entities;

import java.util.*;

/**
 * Entities class for hand, which contains the tiles belong to a specific player
 */
public class Hand {
  private List<Tile> tiles;
  private List<Meld> melds;

  public Hand() {
    tiles = new ArrayList<>();
    melds = new ArrayList<>();
  }

  Hand(List<Tile> tiles) {
    this();
    this.tiles.addAll(tiles);
  }

  /**
   * Add a new Tile into a player hand
   */
  public void addTile(Tile tile) {
    tiles.add(tile);
  }

  public void addTiles(Tile ...tiles) {
    this.tiles.addAll(Arrays.asList(tiles));
  }

  public Tile removeTile(int index) {
    return tiles.remove(index);
  }

  public List<Tile> getTiles() {
    return Collections.unmodifiableList(tiles);
  }

  public List<Meld> getMelds() {
    return Collections.unmodifiableList(melds);
  }

  public Meld formMeld(int ...tileIndexes) {
    Arrays.sort(tileIndexes);
    Tile[] meldTiles = new Tile[tileIndexes.length];
    for (int i = tileIndexes.length - 1; i >= 0; i--) {
      if (tileIndexes[i] < 0 || tileIndexes[i] >= tiles.size()) {
        throw new IllegalArgumentException("Invalid tile index!");
      }
      meldTiles[i] = tiles.remove(tileIndexes[i]);
    }
    Meld newMeld = Meld.createMeld(meldTiles);
    melds.add(newMeld);
    return newMeld;
  }

  public Meld removeMeld(int index) {
    if (index >= 0  && index < melds.size()) {
      return melds.remove(index);
    }
    throw new IllegalArgumentException("Invalid meld index!");
  }

  /**
   * Sort the tiles in hand in this particular order:
   * - RED, BLACK, GREEN, ORANGE
   * - Increasing value number
   */
  public void sort() {
    tiles.sort((tile1, tile2) -> tile1.color() != tile2.color()
        ? tile1.color().compareTo(tile2.color())
        : tile1.value() - tile2.value());
  }

  /**
   * Get the number of tiles in the hand
   */
  public int size() {
    return tiles.size();
  }

  /**
   * Get the score added up from the tiles in hand
   */
  public int getScore() {
    int sum = 0;
    for (Tile tile : tiles) {
      sum += tile.value();
    }
    return sum;
  }
}
