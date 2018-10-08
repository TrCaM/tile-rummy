package project.rummy.entities;

import java.util.List;

/**
 * Entity class for meld, which is a run or a set of tiles played by players and can be put on the table
 */
public class Meld {
  private List<Tile> tiles;
  private MeldType type;

  private Meld(List<Tile> tiles, MeldType type) {
    this.tiles = tiles;
    this.type = type;
  }

  public static Meld createMeld(Tile ...tiles) {
    /* @TODO: write tests and implement this method to that it can detect the MeldType */
    // return new Meld(Arrays.asList(tiles), MeldType.RUN);
    throw new UnsupportedOperationException();
  }

  public List<Tile> getTiles() {
    return this.tiles;
  }

  public MeldType getType() {
    return this.type;
  }


  public int getScore() {
    int score = 0;
    for (Tile tile : tiles) {
      score += tile.value();
    }
    return score;
  }
}
