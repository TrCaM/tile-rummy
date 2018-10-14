package project.rummy.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class MeldTest {
    private Meld meld;

    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);

    private final Tile R3 = Tile.createTile(Color.RED, 3);
    private final Tile G3 = Tile.createTile(Color.GREEN, 3);
    private final Tile B3 = Tile.createTile(Color.BLACK, 3);
    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);

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