package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.game.GameState;

import java.util.*;
import java.util.stream.Stream;

/**
 * Entity class for meld, which is a run or a set of tiles played by players and can be put on the table
 */
public class Meld extends Component {
  public static int nextId = 1;

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
    idsToMelds.put(id,this);
  }

  public static Meld getMeldFromId(int meldid, List<Meld> melds){
    for(Meld m: melds){
      if(m.getId() == meldid){
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

  public void setSource (MeldSource source){
    this.source = source;
  }

  public void setTableRow(int row) {
    if (!isValidMeld() || source != MeldSource.TABLE) {
      throw new IllegalStateException("not a valid table meld");
    }
    this.tableRow = row;
  }

  public int getTableRow() {
    return this.tableRow;
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
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    if (tiles.length == 0) {
      throw new IllegalArgumentException("Invalid tiles input");
    }
    if (tiles.length == 1) {
      return new Meld(tiles[0]);
    }
    if (isRun(tiles)) {
      return new Meld(Arrays.asList(tiles), MeldType.RUN);
    } else if (isSet(tiles)) {
      return new Meld(Arrays.asList(tiles), MeldType.SET);
    }
    throw new IllegalArgumentException("Invalid tiles input");
  }

  public static Meld createMeld(List<Tile> tiles) {
    return createMeld(tiles.toArray(new Tile[0]));
  }

  public static boolean canPlayOnTable(Tile ...tiles) {
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    return tiles.length >= 3 && (isRun(tiles) || isSet(tiles));
  }

  public static boolean canFormMeld(Tile ...tiles) {
    if (tiles.length == 0) {
      return false;
    }
    Arrays.sort(tiles, Comparator.comparing(Tile::value));
    return tiles.length == 1 || isRun(tiles) || isSet(tiles);
  }


  /**
   * Check if a meld is a RUN
   */
  private static boolean isRun(Tile[] tiles) {
    //Initialize first tile colour and value to compare
    Color color = tiles[0].color();
    int startValue = tiles[0].value();
    //Check if two consecutive tiles have the same colour and increasing in value
    for (int i = 0; i < tiles.length; i++) {
      if (tiles[i].color() != color || tiles[i].value() != startValue + i) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if a meld is a SET
   */
  private static boolean isSet(Tile[] tiles) {
    //Check for existing colours in a meld (no duplicate)
    Set<Color> existedColors = new HashSet<>();
    int value = tiles[0].value();
    for (Tile tile : tiles) {
      if (!existedColors.add(tile.color()) || tile.value() != value) {
        return false;
      }
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
    for (Tile tile : tiles) {
      score += tile.value();
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
