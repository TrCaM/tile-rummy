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

  public HandData toHandData() {
    return new HandData(this);
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

  /**
   * find all possible sets that can be made up from a list of tiles
   */
  public List<Meld> findPossibleSets(List<Tile> tilesList){

    List<Meld> possibleMelds = new ArrayList<>();
    List<Tile> alist = new ArrayList<>(tilesList);

    while(alist.size() > 2) {
      List<Tile> tempMeld = new ArrayList<>();
      tempMeld.add(alist.get(0));

      for (int i = 1; i < alist.size(); i++) {
        if (alist.get(0).value() == alist.get(i).value()
                && alist.get(0).color() != alist.get(i).color()
                && !tempMeld.contains(alist.get(i))) {
          tempMeld.add(alist.get(i));
        }
      }

      if (tempMeld.size() >= 3) {
        possibleMelds.add(Meld.createMeld(tempMeld));
      }
      for(Tile e: tempMeld){
        alist.remove(alist.indexOf(e));
      }
    }
    return possibleMelds;
  }

  /**
   * find all possible runs that can be made up from a list of tiles
   */
  public List<Meld> findPossibleRuns(List<Tile> tilesList){

    List<Meld> possibleMelds = new ArrayList<>();
    List<Tile> alist = new ArrayList<>(tilesList);
    alist.sort(Comparator.comparing(Tile::value));

    while(alist.size() > 2) {
      List<Tile> tempMeld = new ArrayList<>();
      tempMeld.add(alist.get(0));

      for (int i = 1; i < alist.size(); i++) {
        if(tempMeld.get(0).color() == alist.get(i).color()
                && !tempMeld.contains(alist.get(i))
                && tempMeld.get(tempMeld.size()-1).value() == alist.get(i).value()-1){
          tempMeld.add(alist.get(i));
        }
      }

      if (tempMeld.size() >= 3) {
        possibleMelds.add(Meld.createMeld(tempMeld));
      }
      for(Tile e: tempMeld){
        alist.remove(alist.indexOf(e));
      }
    }
    return possibleMelds;
  }


  /**
   * find all possible sets and runs from the hand's tiles that give the highest score
   * list of melds 1: find all possible sets first, then runs
   * list of melds 2: find all possible runs first, then sets
   * return the list of melds that gives the highest score
   */
  public List<Meld> findBestMelds(){
    List<Tile> tilesList = new ArrayList<>(tiles);

    //sets first then runs
    List<Meld> bestMelds_1 = findPossibleSets(tilesList);

    for(Meld m: bestMelds_1){
      for(Tile t: m.tiles()){
        tilesList.remove(tilesList.indexOf(t));
      }
    }
    bestMelds_1.addAll(findPossibleRuns(tilesList));

    int score_1 = 0;
    for(Meld m: bestMelds_1){
      score_1 += m.getScore();
    }

    //runs first then sets
    tilesList = new ArrayList<>(tiles);
    List<Meld> bestMelds_2 = findPossibleRuns(tilesList);

    for(Meld m: bestMelds_2){
      for(Tile t: m.tiles()){
        tilesList.remove(tilesList.indexOf(t));
      }
    }
    bestMelds_2.addAll(findPossibleSets(tilesList));

    int score_2 = 0;
    for(Meld m: bestMelds_2){
      score_2 += m.getScore();
    }

    return score_1 > score_2 ? bestMelds_1 : bestMelds_2;
  }


}
