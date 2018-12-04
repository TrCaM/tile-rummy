package project.rummy.ai;

import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.MeldType;
import project.rummy.entities.Tile;

import java.util.*;

public class CombinationSeeker {
  private List<Tile> handTiles;
  private List<Meld> tableMelds;

  public CombinationSeeker(List<Tile> handTiles, List<Meld> tableMelds) {
    this.handTiles = handTiles;
    this.tableMelds = tableMelds;
  }

  /**
   * find all possible melds from which tiles can be detached
   * and form a new set with given tiles
   */
  public Map<Meld, Integer> formSet(List<Tile> collectedTiles) {
    List<Meld> copyMelds = new ArrayList<>(tableMelds);
    List<Tile> remainingTiles = HandMeldSeeker.findRemainingTiles(handTiles);
    int tileValue = collectedTiles.get(0).value();
    Color tileColor = collectedTiles.get(0).color();

    //key is meld and value is tile index
    Map<Meld, Integer> map = new HashMap<>();

    for (Color c : Color.values()) {
      if (c != Color.ANY && c != tileColor) {
        boolean tileInHand = false;
        for (Tile t : remainingTiles) {
          if (t.canFillToRun(c, tileValue) && !collectedTiles.contains(t)) {
            collectedTiles.add(t);
            tileInHand = true;
            break;
          }
        }

        if (!tileInHand) {
          int id = TableMeldSeeker.findDetachableIdenticalTile(tileValue, c, copyMelds);
          if (id != 0) {
            Meld m = Meld.getMeldFromId(id, copyMelds);
            for (int i = 0; i < m.tiles().size(); i++) {
              if (m.type() == MeldType.RUN) {
                if (m.tiles().get(i).canFillToRun(c, tileValue)
                    && (i == 0 || i == m.tiles().size() - 1)) {
                  map.put(m, i);
                  copyMelds.remove(m);
                }
              } else if (m.tiles().get(i).isJoker()
                  || m.tiles().get(i).canFillToRun(c, tileValue)) {
                map.put(m, i);
                copyMelds.remove(m);
              }
            }
          }
        }

      }
    }

    return map;
  }


  private int fillToRun(List<Tile> collectedTiles, int value, Color color, List<Meld> melds) {
    List<Tile> remainingTiles = HandMeldSeeker.findRemainingTiles(handTiles);

    for (Tile t : remainingTiles) {
      if (t.canFillToRun(color, value) && !collectedTiles.contains(t)) {
        collectedTiles.add(t);
        return 0;
      }
    }
    return TableMeldSeeker.findDetachableIdenticalTile(value, color, melds);
  }

  /**
   * find all possible melds from which tiles can be detached
   * and form a new run with given tiles
   */
  public Map<Meld, Integer> formRunByDetaching(List<Tile> collectedTiles) {
    List<Meld> copyMelds = new ArrayList<>(tableMelds);

    Color tileColor = collectedTiles.get(0).color();

    Map<Meld, Integer> map = new HashMap<>();

    int rightValue = collectedTiles.get(0).value() + 1;
    int leftValue = collectedTiles.get(0).value() - 1;

    boolean hasLeft = true;
    boolean hasRight = true;

    while (hasLeft || hasRight) {
      int meldid = 0;
      int neededValue = 0;

      hasRight = rightValue <= 13;
      hasLeft = leftValue >= 1;

      if (hasRight) {
        int previousSize = collectedTiles.size();
        meldid = fillToRun(collectedTiles, rightValue, tileColor, copyMelds);
        if (meldid == 0 && collectedTiles.size() - previousSize == 0) {
          hasRight = false;
        } else {
          neededValue = rightValue;
          rightValue++;
        }
      }

      if (hasLeft && !hasRight) {
        int previousSize = collectedTiles.size();
        meldid = fillToRun(collectedTiles, leftValue, tileColor, copyMelds);
        if (meldid == 0 && collectedTiles.size() - previousSize == 0) {
          hasLeft = false;
        } else {
          neededValue = leftValue;
          leftValue--;
        }
      }

      if (meldid != 0) {
        Meld m = Meld.getMeldFromId(meldid, tableMelds);
        copyMelds.remove(m);
        for (Tile t : m.tiles()) {
          if (t.canFillToRun(tileColor, neededValue)) {
            map.put(m, m.tiles().indexOf(t));
            if (m.tiles().indexOf(t) == 0 || m.tiles().indexOf(t) == m.tiles().size() - 1) {
              break;
            }
          }
        }

      }
    }
    return map;

  }

  public Map<Meld, Integer> formRunBySplitRight(int tileValue, Color tileColor) {

    Map<Meld, Integer> map = new HashMap<>();
    int rightId = TableMeldSeeker.findRightDetachableTiles(tileValue, tileColor, tableMelds);
    Meld rightMeld = rightId == 0 ? null : Meld.getMeldFromId(rightId, tableMelds);

    if (rightMeld != null) {
      for (Tile t : rightMeld.tiles()) {
        List<Tile> temp = new ArrayList<>(rightMeld.tiles());
        temp.sort(Comparator.comparing(Tile::value));
        int firstValue = temp.get(0).value() - rightMeld.tiles().indexOf(temp.get(0));
        Color actualColor = temp.get(0).color();
        int actualValue = firstValue + rightMeld.tiles().indexOf(t);
        if (actualColor==tileColor && tileValue == actualValue &&
            rightMeld.tiles().size() - rightMeld.tiles().indexOf(t) - 1 >= 2) {
          map.put(rightMeld, rightMeld.tiles().indexOf(t));
          return map;

        }
      }
    }
    return map;
  }

}
