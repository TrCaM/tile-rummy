package project.rummy.entities;

import java.util.List;

/**
 * Entity class for meld, which is a run or a set of tiles played by players and can be put on the table
 */
public class Meld {
  private List<Tile> tiles;
  private MeldType type;
  private MeldSource source;

  private Meld(List<Tile> tiles, MeldType type) {
    this.tiles = tiles;
    this.type = type;
  }

  /**
   * Create a meld or a portion of meld by grouping a list of tiles. The creation is valid when:
   *  + There's only tile in the meld: It is a SINGLE meld tyle
   *  + There's two tiles in the meld: isValidMeldPart should return true, but not isValidMeld
   *  + There's three tiles in the meld: isValidMeldPart and isValidMeld both return true.
   */
  public static Meld createMeld(Tile ...tiles) {
    /* @TODO: write tests and implement this method to that it can detect the MeldType */
    // return new Meld(Arrays.asList(tiles), MeldType.RUN);
    throw new UnsupportedOperationException();
  }

  /**
   * Check if this is a valid partial meld.
   */
  private boolean isValidMeldPart() {
    // TODO: Write tests and implement the method
    throw new UnsupportedOperationException();
  }

  /**
   * Check if this a valid meld that can be played on the table.
   */
  public boolean isValidMeld() {
    // TODO: Write tests and implement the method
    throw new UnsupportedOperationException();
  }

  public List<Tile> tiles() {
    return this.tiles;
  }

  public MeldType type() {
    return this.type;
  }

  public MeldSource source() {
    return this.source;
  }

  public int getScore() {
    int score = 0;
    for (Tile tile : tiles) {
      score += tile.value();
    }
    return score;
  }
}
