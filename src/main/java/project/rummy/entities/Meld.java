package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.game.GameState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * Entity class for meld, which is a run or a set of tiles played by players and can be put on the table
 */
public class Meld extends Component implements Serializable {
  private static int nextId = 1;

  public static Map<Integer, Meld> idsToMelds = new HashMap<>();

  private int id;
  private List<Tile> tiles;
  private MeldType type;
  private MeldSource source;
  private int tableRow;


  public Meld(Tile tile) {
    this.tiles = Collections.singletonList(tile);
    this.type = MeldType.SINGLE;
    this.source = MeldSource.HAND;
    this.tableRow = -1;
    this.id = nextId;
    nextId++;
    idsToMelds.put(id, this);
  }

  public Meld(List<Tile> tiles, MeldType type) {
    this.tiles = tiles;
    this.type = type;
    this.source = MeldSource.HAND;
    this.tableRow = -1;
    this.id = nextId;
    nextId++;
    idsToMelds.put(id, this);
  }

  public static int getNextId() {
    return nextId;
  }

  public static void syncMeldId(int id) {
    nextId = id;
  }

  public static Meld getMeldFromId(int meldid, List<Meld> melds) {
    for (Meld m : melds) {
      if (m.getId() == meldid) {
        return m;
      }
    }
    throw new IllegalArgumentException("meld Id not found");
  }

  public static void cleanUpMap(GameState state) {
    HashMap<Integer, Meld> idsToMeldUpdate = new HashMap<>();
    state.getTableData().melds.forEach(meld -> idsToMeldUpdate.put(meld.id, meld));
    Stream.of(state.getHandsData())
        .map(data -> data.melds)
        .forEach(melds -> melds.forEach(meld -> idsToMeldUpdate.put(meld.id, meld)));
    idsToMelds = idsToMeldUpdate;
  }

  public int getId() {
    return id;
  }

  public void setSource(MeldSource source) {
    this.source = source;
  }

  public Tile getTile(int index) {
    return tiles.get(index);
  }

  /**
   * Create a meld or a portion of meld by grouping a list of tiles. The creation is valid when:
   * + There's only tile in the meld: It is a SINGLE meld tyle
   * + There's two tiles in the meld: isValidMeldPart should return true, but not isValidMeld
   * + There's three tiles in the meld: isValidMeldPart and isValidMeld both return true.
   */
  public static Meld createMeld(Tile... tiles) {
    if (tiles.length > 13) {
      throw new IllegalArgumentException("Exceeded number of tiles in a meld!");
    }
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    if (tiles.length == 0) {
      throw new IllegalArgumentException("Invalid tiles input");
    }
    if (tiles.length == 1) {
      return new Meld(tiles[0]);
    }
    if (isSet(tiles)) {
      return new Meld(Arrays.asList(tiles), MeldType.SET);
    } else if (isRun(tiles)) {
      return new Meld(orderRun(tiles), MeldType.RUN);
    }

    throw new IllegalArgumentException(String.format("Invalid tiles input: %s", Arrays.deepToString(tiles)));
  }

  private static List<Tile> orderRun(Tile[] tiles) {
    List<Tile> orderTiles = new ArrayList<>();

    Color color = tiles[0].color();
    int startValue = tiles[0].value();
    int currentIndex = 0;
    int jokerIndex = tiles.length - 1;
    //Check if two consecutive tiles have the same colour and increasing in value
    for (int i = 0; i < tiles.length; i++) {
      if (startValue + i > 13) {
        orderTiles.add(0, tiles[jokerIndex--]);
      } else if (!tiles[currentIndex].canFillToRun(color, startValue + i)) {
        orderTiles.add(tiles[jokerIndex--]);
      } else {
        orderTiles.add(tiles[currentIndex++]);
      }
    }
    return orderTiles;
  }

  public static Meld createMeld(List<Tile> tiles) {
    return createMeld(tiles.toArray(new Tile[0]));
  }

  public static boolean canPlayOnTable(Tile... tiles) {
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    return tiles.length >= 3 && (isRun(tiles) || isSet(tiles));
  }

  private static boolean canFormMeld(Tile... tiles) {
    if (tiles.length == 0) {
      return false;
    }
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    return tiles.length == 1 || isRun(tiles) || isSet(tiles);
  }


  /**
   * Check if a meld is a RUN
   * Assume that the array of Tiles has been sorted (Joker is always at the end)
   */

  private static boolean isRun(Tile[] tiles) {
    //Initialize first tile colour and value to compare
    if (tiles.length > 13) {
      return false;
    }
    int startValue = 30;
    int startIndex = 0;

    while (startValue > 13) {
      startValue = tiles[startIndex].value - startIndex;
      startIndex++;
    }
    Color color = tiles[startIndex - 1].color();
    int currentIndex = 0;
    int jokerIndex = tiles.length - 1;
    //Check if two consecutive tiles have the same colour and increasing in value
    for (int i = 0; i < tiles.length; i++) {
      if (!tiles[currentIndex].canFillToRun(color, startValue + i)) {
        if (!tiles[jokerIndex].canFillToRun(color, startValue + i)) {
          return false;
        }
        jokerIndex--;
      } else {
        currentIndex++;
      }
    }
    return true;
  }

  /**
   * Check if a meld is a SET
   */
  private static boolean isSet(Tile[] tiles) {
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    int tileValue = tiles[0].value;
    if (tiles.length > 4) {
      return false;
    }
    Set<Color> existingColors = new HashSet<>();
    for (Color c : Color.values()) {
      if (c != Color.ANY) {
        existingColors.add(c);
      }
    }

    for (Tile t : tiles) {
      if (!t.canFillToSet(existingColors, tileValue)) {
        return false;
      }
      existingColors.remove(t.color);
    }
    return true;
  }

  /**
   * Check if this a valid meld that can be played on the table.
   */
  public boolean isValidMeld() {
    return tiles.size() >= 3;
  }

  public List<Tile> tiles() {
    return Collections.unmodifiableList(tiles);
  }

  public MeldType type() {
    return this.type;
  }

  public MeldSource source() {
    return this.source;
  }

  public int getScore() {
    int score = 0;

    if (isSet(tiles.toArray(new Tile[0]))) {
      score = tiles.get(0).value * tiles.size();
    } else if (isRun(tiles.toArray(new Tile[0]))) {
      int firstTile = 0;
      for (int i = 0; i < tiles.size(); i++) {
        if (!tiles.get(i).isJoker()) {
          firstTile = tiles.get(i).value - i;
          break;
        }
      }
      for (int k = firstTile; k < firstTile + tiles.size(); k++) {
        score += k;
      }
    } else {
      throw new IllegalArgumentException("Invalid meld");
    }

    return score;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Meld && ((Meld) obj).id == this.id;
  }
}
