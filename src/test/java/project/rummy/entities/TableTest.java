package project.rummy.entities;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
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
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);

  private Table table = new Table();
  private Table shuffleTable = new Table();

  /**
   * The reason I placed this up here is to be able to modify this later with sizes.
   */
  static private final int TILES_AMOUNT = 104;
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
      assertThat(tile.value(), lessThanOrEqualTo(Table.MAX_VALUE));
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
    for (Tile tile : tiles) {
      System.out.println(tile_value + "   |   " + tile.value());
      assertEquals(tile.value(), tile_value);

      tile_value++;
      if (tile_value > 13) {
        tile_value = 1;
        sets++;
      }
    }

    assertEquals(sets, SETS_AMOUNT);
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
      assertThat(tile.value(), lessThanOrEqualTo(Table.MAX_VALUE));
      assertNotNull(tile.color());
    }
  }

  @Test
  public void addMeld_shouldSucceed() {
    Meld validMeld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld validMeld2 = Meld.createMeld(R3, B3, G3);
    Meld invalidMeld = Meld.createMeld(R3, B3);

    assertTrue(table.addMeld(validMeld1));
    assertTrue(table.addMeld(validMeld2));
    assertFalse(table.addMeld(invalidMeld));

    assertThat(table.getPlayingMelds(), contains(validMeld1, validMeld2));
    assertThat(table.getPlayingMelds(), not(contains(invalidMeld)));

    assertTrue(table.getSetGrid1()[2][0] == validMeld2.getId());
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
}
