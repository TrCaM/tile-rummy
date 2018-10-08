package project.rummy.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represent the table entity of the game
 */
public class Table {
  private List<Tile> freeTiles;
  private List<Meld> melds;
  private int MAX_VALUE = 13;
  private int DECKS_AMOUNT = 2;



  public void initTiles() {
      freeTiles = new ArrayList<>();
      createDeck();
  }
  public void shuffle() {
      Collections.shuffle(freeTiles);
  }

  private void createDeck() {
    for (int j = 0; j < DECKS_AMOUNT; j++) {
        for (Color color : Color.values()) {
            for (int i = 1; i <= MAX_VALUE; i++) {
                this.freeTiles.add(Tile.createTile(color, i));
            }
        }
    }
  }


  public List<Tile> getFreeTiles(){
    return this.freeTiles;
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
