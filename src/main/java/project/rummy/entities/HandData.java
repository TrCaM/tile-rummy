package project.rummy.entities;

import java.util.List;

public class HandData {
  public List<Tile> tiles;
  public List<Meld> melds;

  HandData(Hand hand) {
    this.tiles = hand.getTiles();
    this.melds = hand.getMelds();
  }
}

