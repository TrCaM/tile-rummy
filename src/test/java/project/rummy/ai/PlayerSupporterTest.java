package project.rummy.ai;

import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerSupporterTest {


    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile R5 = Tile.createTile(Color.RED, 5);
    private final Tile R6 = Tile.createTile(Color.RED, 6);
    private final Tile R7 = Tile.createTile(Color.RED, 7);
    private final Tile O10 = Tile.createTile(Color.ORANGE, 10);
    private final Tile O12 = Tile.createTile(Color.ORANGE, 12);
    private final Tile O13 = Tile.createTile(Color.ORANGE, 13);
    private final Tile R3 = Tile.createTile(Color.RED, 3);
    private final Tile R8 = Tile.createTile(Color.RED, 8);
    private final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private final Tile G11 = Tile.createTile(Color.GREEN, 11);
    private final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private final Tile B11 = Tile.createTile(Color.BLACK, 11);
    private final Tile R11 = Tile.createTile(Color.RED, 11);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile JK = Tile.createTile(Color.ANY, 0);
    private final Tile JK2 = Tile.createTile(Color.ANY, 0);

    @Test
    public void suggestFormMeld_test(){

        List<Tile> tiles = Arrays.asList(O5, O10, O6, O7, R3,R11);

        boolean suggestion = PlayerSupporter.suggestFormMeld(tiles);

        assertEquals(true, suggestion);
        assertEquals(true, tiles.get(0).isSuggested());
        assertEquals(true, tiles.get(2).isSuggested());
        assertEquals(true, tiles.get(3).isSuggested());
        assertEquals(false, tiles.get(4).isSuggested());
        assertEquals(false, tiles.get(5).isSuggested());

    }

    @Test
    public void suggestManipulationSet_test(){

        List<Tile> tiles = Arrays.asList(O5, O10);
        List<Meld> melds = new ArrayList<>();
        melds.add(Meld.createMeld(B5, G5, O5, R5));
        melds.add(Meld.createMeld(R5, R6, R7, R8));

        boolean suggestion = PlayerSupporter.suggestManipulationSet(tiles,melds);




    }
}
