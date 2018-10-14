package project.rummy.entities;

import org.junit.Test;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

public class MeldTest {
  private Meld meld;

  private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile R3dub = Tile.createTile(Color.RED, 3);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);
  private static final Tile O3 = Tile.createTile(Color.ORANGE, 3);

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
    Meld.createMeld(R3dub, R3);
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
  //testing score of a run
  public void getScore_Run() {
    meld = Meld.createMeld(O5, O6, O7);
    assertEquals(meld.getScore(), 18);

    meld = Meld.createMeld(O5, O6, O7, O8);
    assertEquals(meld.getScore(), 26);
  }

  @Test
  //testing score of a set
  public void getScore_Set() {
    meld = Meld.createMeld(R3, G3, B3);
    assertEquals(meld.getScore(), 9);

    meld = Meld.createMeld(R3, G3, B3, O3);
    assertEquals(meld.getScore(), 12);
  }
}