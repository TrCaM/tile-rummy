package project.rummy.entities;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.game.GameState;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MeldTest {
  private Meld meld;

  private static final Tile O1 = Tile.createTile(Color.ORANGE, 1);
  private static final Tile O2 = Tile.createTile(Color.ORANGE, 2);
  private static final Tile O3 = Tile.createTile(Color.ORANGE, 3);
  private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
  private static final Tile O13 = Tile.createTile(Color.ORANGE, 13);
  private static final Tile O11 = Tile.createTile(Color.ORANGE, 11);
  private static final Tile O12 = Tile.createTile(Color.ORANGE, 12);
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);
  private static final Tile JK = Tile.createTile(Color.ANY, 0);
  private static final Tile JK2 = Tile.createTile(Color.ANY, 0);

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private GameState gameState;

  @Test(expected = IllegalArgumentException.class)
  public void createMeld_2tiles_shouldThrow() {
    Meld.createMeld(O5, O9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMeld_invalidTiles_shouldThrow() {
    Meld.createMeld(O5, O6, B3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMeld_noTiles_shouldThrow() {
    Meld.createMeld();
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMeld_invalidSet_shouldThrow() {
    Meld.createMeld(R3, R3);
  }

  @Test
  public void createMeld_1tile_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(G3);
    assertEquals(expectedMeld.type(), MeldType.SINGLE);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(G3));
  }

  @Test
  public void createMeld_2tilesRun_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(O5, O6);
    assertEquals(expectedMeld.type(), MeldType.RUN);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(O5, O6));
  }

  @Test
  public void createMeld_2tilesSet_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(O3, R3);
    assertEquals(expectedMeld.type(), MeldType.SET);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(O3, R3));
  }

  @Test
  public void createMeld_runWith3Tiles_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(O5, O6, O7);
    assertEquals(expectedMeld.type(), MeldType.RUN);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(O5, O6, O7));
  }

  @Test
  public void createMeld_runWith5Tiles_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(O5, O6, O7, O8, O9);
    assertEquals(expectedMeld.type(), MeldType.RUN);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(O5, O6, O7, O8, O9));
  }

  @Test
  public void createMeld_setWith3Tiles_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(R3, G3, B3);
    assertEquals(expectedMeld.type(), MeldType.SET);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(R3, G3, B3));
  }

  @Test
  public void createMeld_setWith4Tiles_shouldSucceed() {
    Meld expectedMeld = Meld.createMeld(R3, G3, B3, O3);
    assertEquals(expectedMeld.type(), MeldType.SET);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertThat(expectedMeld.tiles(), contains(R3, G3, B3, O3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void createMeld_invalid3TilesSet_shouldSucceed() {
    Meld.createMeld(R3, R3, B3);
  }

  @Test
  public void getScore_Run() {
    meld = Meld.createMeld(O5, O6, O7);
    assertEquals(meld.getScore(), 18);

    meld = Meld.createMeld(O5, O6, O7, O8);
    assertEquals(meld.getScore(), 26);
  }

  @Test
  public void getScore_Set() {
    meld = Meld.createMeld(R3, G3, B3);
    assertEquals(meld.getScore(), 9);

    meld = Meld.createMeld(R3, G3, B3, O3);
    assertEquals(meld.getScore(), 12);
  }

  @Test
  public void isValidMeld_shouldReturnTrue() {
    meld = Meld.createMeld(R3, G3, B3);
    assertTrue(meld.isValidMeld());
  }

  @Test
  public void isValidMeld_shouldReturnFalse() {
    meld = Meld.createMeld(R3, B3);
    assertFalse(meld.isValidMeld());
  }

  @Mock
  private Hand hand;


  @Test
  public void resetMapTest_shoudSuceed() {
    Meld meld = Meld.createMeld(O5, O6, O7);
    Meld meld2 = Meld.createMeld(R3,G3, B3, O3);
    Table table = new Table();
    table.addMeld(meld);
    table.addMeld(meld2);
    when(gameState.getTableData()).thenReturn(table.toTableData());
    when(gameState.getHandsData()).thenReturn(new HandData[0]);

    Meld.cleanUpMap(gameState);

    assertThat(Meld.idsToMelds.get(meld.getId()), is(meld));
    assertThat(Meld.idsToMelds.get(meld2.getId()), is(meld2));
  }


  @Test(expected = IllegalArgumentException.class)
  public void createMeld_invalidJoker_shouldThrow() {
    Meld.createMeld(O5, O9, JK);
  }


  @Test
  public void createMeld_JokerSet() {
    Meld expectedMeld = Meld.createMeld(R3, G3, B3, JK);
    assertEquals(expectedMeld.type(), MeldType.SET);
    assertEquals(expectedMeld.source(), MeldSource.HAND);
    assertTrue(expectedMeld.tiles().contains(JK));
    assertTrue(expectedMeld.tiles().size() == 4);

    Meld expectedMeld2 = Meld.createMeld(R3, G3, JK);
    assertEquals(expectedMeld2.type(), MeldType.SET);
    assertTrue(expectedMeld2.tiles().contains(JK));
    assertTrue(expectedMeld2.tiles().size() == 3);

    Meld expectedMeld3 = Meld.createMeld(R3, JK, JK2);
    assertEquals(expectedMeld3.type(), MeldType.SET);
    assertTrue(expectedMeld3.tiles().contains(JK2));
    assertTrue(expectedMeld3.tiles().size() == 3);

    Meld expectedMeld5 = Meld.createMeld(JK, JK2);
    assertEquals(expectedMeld5.type(), MeldType.SET);
    assertTrue(expectedMeld5.tiles().contains(JK));
    assertTrue(expectedMeld5.tiles().contains(JK2));

  }

  @Test
  public void createMeld_JokerRun() {
    Meld expectedMeld = Meld.createMeld(O1, O2, O3, JK);
    assertEquals(expectedMeld.type(), MeldType.RUN);
    assertTrue(expectedMeld.tiles().contains(JK));
    assertTrue(expectedMeld.tiles().size() == 4);
    assertTrue(expectedMeld.tiles().get(0) == O1);
    assertTrue(expectedMeld.tiles().get(1) == O2);
    assertTrue(expectedMeld.tiles().get(2) == O3);
    assertTrue(expectedMeld.tiles().get(3) == JK);

    Meld expectedMeld2 = Meld.createMeld(O5, O6, O7, O9, JK);
    assertEquals(expectedMeld2.type(), MeldType.RUN);
    assertTrue(expectedMeld2.tiles().contains(JK));
    assertTrue(expectedMeld2.tiles().size() == 5);
    assertTrue(expectedMeld2.tiles().get(0) == O5);
    assertTrue(expectedMeld2.tiles().get(1) == O6);
    assertTrue(expectedMeld2.tiles().get(2) == O7);
    assertTrue(expectedMeld2.tiles().get(3) == JK);
    assertTrue(expectedMeld2.tiles().get(4) == O9);

    Meld expectedMeld3 = Meld.createMeld(O5, O7, O9, JK, JK2);
    assertEquals(expectedMeld3.type(), MeldType.RUN);
    assertTrue(expectedMeld3.tiles().contains(JK2));
    assertTrue(expectedMeld3.tiles().size() == 5);
    assertTrue(expectedMeld3.tiles().get(0) == O5);
    assertTrue(expectedMeld3.tiles().get(1) == JK);
    assertTrue(expectedMeld3.tiles().get(2) == O7);
    assertTrue(expectedMeld3.tiles().get(3) == JK2);
    assertTrue(expectedMeld3.tiles().get(4) == O9);


    Meld expectedMeld4 = Meld.createMeld(O13, O12, O11, JK);
    assertEquals(expectedMeld4.type(), MeldType.RUN);
    assertTrue(expectedMeld4.tiles().contains(JK));
    assertTrue(expectedMeld4.tiles().size() == 4);
    assertTrue(expectedMeld4.tiles().get(0) == JK);
    assertTrue(expectedMeld4.tiles().get(1) == O11);
    assertTrue(expectedMeld4.tiles().get(2) == O12);
    assertTrue(expectedMeld4.tiles().get(3) == O13);




  }
}