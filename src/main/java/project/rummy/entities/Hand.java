package project.rummy.entities;

import java.util.List;

/**
 * Entities class for hand, which contains the tiles belong to a specific player
 */
public class Hand {
  private List<Tile> tiles;

  public List<Tile> getTiles() {
    return this.tiles;
  }

  /**
   * @TODO: Write tests and then implement the method
   * @param tile
   */
  public void addTile(Tile tile) {
    throw new UnsupportedOperationException();
  }

  public Tile removeTile(int index) {
    return tiles.remove(index);
  }

  /**
   * Sort the tiles in hand in this particular order:
   *   - RED, BLACK, GREEN, ORDER
   *   - Increasing value number
   * TODO: Write tests and then implement the method
   */
  public void sort() {
    throw new UnsupportedOperationException();
  }

  /**
   * Get the number of tiles in the hand
   */
  public int size() {
    return tiles.size();
  }

  /**
   * Get the score added up from the tiles in hand
   * TODO: Write tests and then implement the method
   */
  public int getScore() {
    throw new UnsupportedOperationException();
  }
}
