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
    throw new UnsupportedOperationException();
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
    List<Tile> sortedList = new ArrayList<>();

    //sort tiles list in the increasing order of tile values
    Collections.sort(tiles, new Comparator<Tile>() {
      @Override
      public int compare(Tile tile1, Tile tile2) {
        return tile1.value() - tile2.value();
      }
    });

    //RED tiles come first
    for (Tile tile : tiles) {
      if(tile.color().equals(Color.RED)){
        sortedList.add(tile);
      }
    }

    //BLACK tiles next
    for (Tile tile : tiles) {
      if(tile.color().equals(Color.BLACK)){
        sortedList.add(tile);
      }
    }

    //GREEN tiles
    for (Tile tile : tiles) {
      if(tile.color().equals(Color.GREEN)){
        sortedList.add(tile);
      }
    }

    //ORANGE tiles to the last
    for (Tile tile : tiles) {
      if(tile.color().equals(Color.ORANGE)){
        sortedList.add(tile);
      }
    }

    tiles = sortedList;
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
