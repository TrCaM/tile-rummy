package project.rummy.entities;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

public class TableTest {
  private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile R7 = Tile.createTile(Color.RED, 7);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile G7 = Tile.createTile(Color.GREEN, 7);
  private static final Tile G9 = Tile.createTile(Color.GREEN, 9);
  private static final Tile B1 = Tile.createTile(Color.BLACK, 1);
  private static final Tile B2 = Tile.createTile(Color.BLACK, 2);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);
  private static final Tile B5 = Tile.createTile(Color.BLACK, 5);
  private static final Tile B6 = Tile.createTile(Color.BLACK, 6);
  private static final Tile B7 = Tile.createTile(Color.BLACK, 7);
  private static final Tile JK1 = Tile.createTile(Color.ANY, 0);
  private static final Tile JK2 = Tile.createTile(Color.ANY, 0);


  private Table table = new Table();
  private Table shuffleTable = new Table();

  /**
   * The reason I placed this up here is to be able to modify this later with sizes.
   */
  static private final int TILES_AMOUNT = 106;
  static private final int SETS_AMOUNT = 8;
  static private final int SUITS_AMOUNT = 26;

  @Before
  public void setUp() {
    table.initTiles();
    shuffleTable.initTiles();
  }

  @Test
  public void check_Initialized_Tiles() {
    // test if the list works
    assertNotNull(table.getFreeTiles());

    // check for correct size
    assertEquals(table.getFreeTiles().size(), TILES_AMOUNT);

    // check for individually aren't set to null
    for (Tile tile : table.getFreeTiles()) {
      assertThat(tile.value(), greaterThanOrEqualTo(0));
      assertThat(tile.value(), lessThanOrEqualTo(30));
      assertNotNull(tile.color());
    }


  }

  @Test
  public void check_tiles_values() {
    // this test if the size if correctly implemented
    List<Tile> tiles = table.getFreeTiles();
    int tile_value = 1;
    int sets = 0;

    System.out.println("EV  |  RV");
    int jokers = 0;
    for (Tile tile : tiles) {
      if (tile.value == 30) {
        jokers++;
      } else {
        System.out.println(tile_value + "   |   " + tile.value());
        assertEquals(tile.value(), tile_value);

        tile_value++;
        if (tile_value > 13) {
          tile_value = 1;
          sets++;
        }
      }
    }

    assertEquals(sets, SETS_AMOUNT);
    assertEquals(jokers, 2);
  }

  @Test
  public void check_Tiles_Colors() {
    int[] amount = new int[4];

    int[] expected_amount = new int[4];

    for (int i = 0; i < expected_amount.length; i++) {
      expected_amount[i] = SUITS_AMOUNT;
      System.out.println(expected_amount[i]);
    }


    for (Tile tile : table.getFreeTiles()) {
      if (tile.color().name().equals(Color.BLACK.toString())) {
        amount[0]++;
      } else if (tile.color().name().equals(Color.ORANGE.toString())) {
        amount[1]++;
      } else if (tile.color().name().equals(Color.RED.toString())) {
        amount[2]++;
      } else if (tile.color().name().equals(Color.GREEN.toString())) {
        amount[3]++;
      }
    }

    assertArrayEquals(expected_amount, amount);


  }

  /**
   * This is to check if the method actually shuffled, as a test work around
   * This shuffle is a different method due to testing
   */

  @Test
  public void check_shuffled() {
    shuffleTable.shuffle();
    boolean isShuffled = false;
    int tile_unmoved = 0;
    List<Tile> unshuffledTiles = table.getFreeTiles();
    List<Tile> shuffledTiles = shuffleTable.getFreeTiles();

    assertEquals(shuffledTiles.size(), unshuffledTiles.size());

    for (int i = 0; i < unshuffledTiles.size(); i++) {
      if (!(unshuffledTiles.get(i).color().name().equals(shuffledTiles.get(i).color().name()))
          || !(unshuffledTiles.get(i).value() == shuffledTiles.get(i).value())) {
        isShuffled = true;
      } else if ((unshuffledTiles.get(i).color().name().equals(shuffledTiles.get(i).color().name()))
          && (unshuffledTiles.get(i).value() == shuffledTiles.get(i).value())) {
        tile_unmoved++;
      }
    }
    assertTrue(isShuffled);
    System.out.println("Tiles that Remain in the same location: " + tile_unmoved);

    for (Tile tile : shuffleTable.getFreeTiles()) {
      assertThat(tile.value(), greaterThanOrEqualTo(0));
      assertThat(tile.value(), lessThanOrEqualTo(30));
      assertNotNull(tile.color());
    }
  }

  @Test
  public void addMeld_shouldSucceed() {
    Meld validRun2 = Meld.createMeld(B1, B2, B3);
    Meld validRun4 = Meld.createMeld(B1, B2, B3);
    Meld validSet1 = Meld.createMeld(R3, B3, G3);
    Meld validSet2 = Meld.createMeld(O7, B7, G7, R7);
    Meld validSet3 = Meld.createMeld(O7, B7, G7);
    Meld validJokerMeld1 = Meld.createMeld(O7, B7, JK1);
    Meld validJokerMeld2 = Meld.createMeld(G7, JK2, G9);

    assertTrue(table.addMeld(validSet1));
    assertTrue(table.addMeld(validSet2));
    assertTrue(table.addMeld(validSet3));
    assertTrue(table.addMeld(validRun2));
    assertTrue(table.addMeld(validRun4));
    assertTrue(table.addMeld(validJokerMeld1));
    assertTrue(table.addMeld(validJokerMeld2));

    assertEquals(table.getSetGrid1()[2][0], validSet1.getId());
    assertEquals(table.getSetGrid1()[6][0], validSet2.getId());
    assertEquals(table.getSetGrid2()[6][0], validSet3.getId());

    assertEquals(table.getRunGrid()[2][0], validRun2.getId());
    assertEquals(table.getRunGrid()[3][0], validRun4.getId());

    assertEquals(table.getRunGrid()[11][0], validJokerMeld1.getId());
    assertEquals(table.getRunGrid()[12][0], validJokerMeld2.getId());
  }

  @Test
  public void backupMelds_shouldStoreMelds() {
    Meld validMeld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld validMeld2 = Meld.createMeld(R3, B3, G3);
    table.addMeld(validMeld1);
    table.addMeld(validMeld2);
    table.backupMelds();

    List<Meld> backupMeld = table.getBackupMelds();
    assertEquals(table.getBackupMelds().size(), 2);
    assertThat(backupMeld.get(0).tiles(), contains(O5, O6, O7, O8));
    assertThat(backupMeld.get(1).tiles(), contains(R3, B3, G3));
  }

  @Test
  public void removeRunFromGrid() {
    Meld run1 = Meld.createMeld(B1, B2, B3);
    Meld run2 = Meld.createMeld(B1, B2, B3);
    Meld run3 = Meld.createMeld(B5, B6, B7);

    assertTrue(table.addMeld(run1));
    assertTrue(table.addMeld(run2));
    assertTrue(table.addMeld(run3));

    table.removeMeld(run1);
    table.removeMeld(run3);

    assertEquals(0, table.getRunGrid()[2][0]);
    assertEquals(0, table.getRunGrid()[2][4]);
    assertEquals(table.getRunGrid()[3][0], run2.getId());
  }

  @Test
  public void removeSetFromGrid() {
    Meld set1 = Meld.createMeld(O7, B7, G7);
    Meld set2 = Meld.createMeld(O7, B7, G7);

    assertTrue(table.addMeld(set1));
    assertTrue(table.addMeld(set2));

    assertEquals(table.getSetGrid1()[6][0], set1.getId());
    assertEquals(table.getSetGrid2()[6][0], set2.getId());

    table.removeMeld(set1);
    table.removeMeld(set2);

    assertEquals(0, table.getSetGrid1()[6][0]);
    assertEquals(0, table.getSetGrid1()[6][0]);
  }

}
