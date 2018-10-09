package project.rummy.entities;

import org.junit.Test;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

public class MeldTest {
    private Meld meld;

    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile O9 = Tile.createTile(Color.ORANGE, 9);

    private final Tile R3 = Tile.createTile(Color.RED, 3);
    private final Tile R3dub = Tile.createTile(Color.RED, 3);
    private final Tile G3 = Tile.createTile(Color.GREEN, 3);
    private final Tile B3 = Tile.createTile(Color.BLACK, 3);
    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);

    @Test(expected = IllegalArgumentException.class)
    public void createMeld_2tiles_shouldThrow() {
        Meld.createMeld(O5, O6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMeld_invalidTiles_shouldThrow() {
        Meld.createMeld(O5, O6, B3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createMeld_noTiles_shouldThrow() {
        Meld.createMeld();
    }

    @Test()
    public void createMeld_runWith3Tiles_shouldSucceed() {
        Meld expectedMeld = Meld.createMeld(O5, O6, O7);
        assertEquals(expectedMeld.getType(), MeldType.RUN);
        assertThat(expectedMeld.getTiles(), contains(O5, O6, O7));
    }

    @Test()
    public void createMeld_runWith5Tiles_shouldSucceed() {
        Meld expectedMeld = Meld.createMeld(O5, O6, O7,O8, O9);
        assertEquals(expectedMeld.getType(), MeldType.RUN);
        assertThat(expectedMeld.getTiles(), contains(O5, O6, O7, O8, O9));
    }

    @Test()
    public void createMeld_setWith3Tiles_shouldSucceed() {
        Meld expectedMeld = Meld.createMeld(R3, G3, B3);
        assertEquals(expectedMeld.getType(), MeldType.SET);
        assertThat(expectedMeld.getTiles(), contains(R3, G3, B3));
    }

    @Test()
    public void createMeld_setWith4Tiles_shouldSucceed() {
        Meld expectedMeld = Meld.createMeld(R3, G3, B3, O3);
        assertEquals(expectedMeld.getType(), MeldType.SET);
        assertThat(expectedMeld.getTiles(), contains(R3, G3, B3, O3));
    }

    @Test (expected = IllegalArgumentException.class)
    public void createMeld_invalid3TilesSet_shouldSucceed() {
         Meld.createMeld(R3, R3dub, B3);
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