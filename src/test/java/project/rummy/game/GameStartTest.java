package project.rummy.game;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** this tests two things GameStart.java and PlayerTileValues.java **/
public class GameStartTest {
    @Before
    public void setUp() {

    }

    /** this tests the playerTileValue class is tested **/
    @Test
    public void checkValidPlayer() {
        PlayerTileValue p_invalid;
        PlayerTileValue p_valid;

        p_invalid = new PlayerTileValue(-1);
        assertFalse(p_invalid.isValidPlayer());

        p_invalid = new PlayerTileValue(5);
        assertFalse(p_invalid.isValidPlayer());

        for (int i = 0; i < 4; i++) {
            p_valid = new PlayerTileValue(i);
            assertTrue(p_valid.isValidPlayer());
        }
    }

    @Test
    public void checkValidSetTotal() {
        ArrayList<Tile> tiles = new ArrayList<>();
        PlayerTileValue tileValue = new PlayerTileValue(0);

        Tile O3 = Tile.createTile(Color.ORANGE, 2);
        Tile O4 = Tile.createTile(Color.ORANGE, 10);
        Tile O5 = Tile.createTile(Color.ORANGE, 11);
        Tile B9 = Tile.createTile(Color.BLACK, 9);
        tiles.add(O3);
        tiles.add(O4);
        tiles.add(O5);
        tiles.add(B9);

        tileValue.setTotalValue(tiles);
        assertEquals(32, tileValue.getTotalValue());

        tiles.add(B9);
        tileValue.setTotalValue(tiles);
        assertEquals(41, tileValue.getTotalValue());
    }

    /** the other tests have to be written as a JSON file to be read **/
}
