package project.rummy.entities;

import java.util.*;

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
    if(tiles.length < 3 ){
      throw new IllegalArgumentException("Not enough tiles");
    }
    if (isRun(tiles)) {
      return new Meld (Arrays.asList(tiles), MeldType.RUN);
    } else if (isSet(tiles)) {
      return new Meld (Arrays.asList(tiles), MeldType.SET);
    }
    throw new IllegalArgumentException("Invalid tiles input");
  }

  //Check if a meld is a RUN
  private static boolean isRun(Tile[] tiles) {
      //Initialize first tile colour and value to compare
    Color color = tiles[0].color();
    int startValue = tiles[0].value();
    //Check if two consecutive tiles have the same colour and increasing in value
    for(int i = 0; i < tiles.length; i++){
      if (tiles[i].color() != color || tiles[i].value() != startValue + i){
        return false;
      }
    }
    return true;
  }

  //Check if a meld is a SET
  private static boolean isSet(Tile[] tiles) {
      //Check for existing colours in a meld (no duplicate)
    Set noDup = new HashSet();
    for(int i = 0; i < tiles.length; i ++){
      noDup.add(tiles[i].color());
    }

    //Something wrong here
      // I'm trying to check if there is existing 2 tiles with the same colour and value
    for(int j = 0; j < tiles.length; j++ ){
      if((noDup.contains(tiles[j].color()) && noDup.contains(tiles[j + 1].color()))
              && (tiles[j].value() == tiles[j-1].value())){
        return false;
      }
    }
      return true;
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
