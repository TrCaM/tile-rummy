package project.rummy.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Entities class for hand, which contains the tiles belong to a specific player
 */
public class Hand {
  private List<Tile> tiles;

  public Hand() {
    tiles = new ArrayList<>();
  }

  Hand(List<Tile> tiles) {
    this.tiles = tiles;
  }

  /**
   * Add a new Tile into a player hand
   * @TODO: Write tests and then implement the method
   */
  public void addTile(Tile tile) {
    tiles.add(tile);
  }

  public Tile removeTile(int index) {
    return tiles.remove(index);
  }

  public List<Tile> getTiles() {
    return this.tiles;
  }

  /**
   * Sort the tiles in hand in this particular order:
   *   - RED, BLACK, GREEN, ORANGE
   *   - Increasing value number
   */
  public void sort() {
      Collections.sort(tiles, (tile1, tile2) -> tile1.color() != tile2.color()
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
