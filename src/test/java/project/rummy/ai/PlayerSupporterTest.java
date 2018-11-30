package project.rummy.ai;

import org.junit.Test;
import org.omg.CORBA.TRANSACTION_MODE;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerSupporterTest {



    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O2 = Tile.createTile(Color.ORANGE, 2);
    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);
    private final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private final Tile O5_2 = Tile.createTile(Color.ORANGE, 5);
    private final Tile R5 = Tile.createTile(Color.RED, 5);
    private final Tile R6 = Tile.createTile(Color.RED, 6);
    private final Tile R7 = Tile.createTile(Color.RED, 7);
    private final Tile O10 = Tile.createTile(Color.ORANGE, 10);
    private final Tile O9 = Tile.createTile(Color.ORANGE, 9);
    private final Tile R3 = Tile.createTile(Color.RED, 3);
    private final Tile G6 = Tile.createTile(Color.GREEN, 6);
    private final Tile R8 = Tile.createTile(Color.RED, 8);
    private final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private final Tile B3 = Tile.createTile(Color.BLACK, 3);
    private final Tile B6 = Tile.createTile(Color.BLACK, 6);
    private final Tile G11 = Tile.createTile(Color.GREEN, 11);
    private final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private final Tile G3 = Tile.createTile(Color.GREEN, 3);
    private final Tile B11 = Tile.createTile(Color.BLACK, 11);
    private final Tile R11 = Tile.createTile(Color.RED, 11);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O7_2 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile JK = Tile.createTile(Color.ANY, 0);
    private final Tile JK2 = Tile.createTile(Color.ANY, 0);

    @Test
    public void suggestFormMeld_test(){

        List<Tile> tiles = Arrays.asList(O5, O10, O6, O7, R3,R11);
        List<Meld> melds = new ArrayList<>();

        boolean suggestion = new PlayerSupporter(tiles, melds).displayHints_1();

        assertEquals(true, suggestion);
        assertEquals(true, tiles.get(0).isSuggested());
        assertEquals(true, tiles.get(2).isSuggested());
        assertEquals(true, tiles.get(3).isSuggested());
        assertEquals(false, tiles.get(4).isSuggested());
        assertEquals(false, tiles.get(5).isSuggested());

    }

    @Test
    public void suggestAddDirectly_test(){

        List<Tile> tiles = Arrays.asList(O5, O10);
        List<Meld> melds = new ArrayList<>();
        melds.add(Meld.createMeld(O6, O7, O8));

        boolean suggestion = new PlayerSupporter(tiles, melds).displayHints_1();

        assertEquals(true, suggestion);
        assertEquals(true, tiles.get(0).isSuggested());

        assertEquals(true, melds.get(0).tiles().get(0).isSuggested());
        assertEquals(true, melds.get(0).tiles().get(1).isSuggested());
        assertEquals(true, melds.get(0).tiles().get(2).isSuggested());

    }

    @Test
    public void suggestManipulationSet_test(){

        List<Tile> tiles = Arrays.asList(O5, O10);
        List<Meld> melds = new ArrayList<>();
        melds.add(Meld.createMeld(R5, R6, R7, R8));
        melds.add(Meld.createMeld(B5, G5, O5_2, R5));

        boolean suggestion = new PlayerSupporter(tiles, melds).displayHints_1();

        assertEquals(true, suggestion);
        assertEquals(true, tiles.get(0).isSuggested());

        assertEquals(true, melds.get(0).tiles().get(0).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(1).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(2).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(3).isSuggested());

        assertEquals(true, melds.get(1).tiles().get(0).isSuggested());
        assertEquals(false, melds.get(1).tiles().get(1).isSuggested());
        assertEquals(false, melds.get(1).tiles().get(2).isSuggested());

    }

    @Test
    public void suggestManipulationRun_test(){

        List<Tile> tiles = Arrays.asList(O7, O10);
        List<Meld> melds = new ArrayList<>();
        melds.add(Meld.createMeld(O6, R6, B6, G6));
        melds.add(Meld.createMeld(B5, G5, O5, R5));

        boolean suggestion = new PlayerSupporter(tiles, melds).displayHints_1();

        assertEquals(true, suggestion);
        assertEquals(true, tiles.get(0).isSuggested());
        assertEquals(false, tiles.get(1).isSuggested());

        assertEquals(true, melds.get(0).tiles().get(0).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(1).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(2).isSuggested());
        assertEquals(false, melds.get(0).tiles().get(3).isSuggested());

        assertEquals(false, melds.get(1).tiles().get(0).isSuggested());
        assertEquals(false, melds.get(1).tiles().get(1).isSuggested());
        assertEquals(true, melds.get(1).tiles().get(2).isSuggested());
        assertEquals(false, melds.get(1).tiles().get(3).isSuggested());
    }

  @Test
  public void suggestManipulationRun_test2(){

    List<Tile> tiles = Arrays.asList(O7_2, B11);
    List<Meld> melds = new ArrayList<>();
    melds.add(Meld.createMeld(O5,O6, O7, O8, O9, O10));
    melds.add(Meld.createMeld(B5, G5, R5));

    PlayerSupporter supporter = new PlayerSupporter(tiles, melds);
    boolean suggestion = supporter.displayHints_1();

    assertEquals(true, suggestion);
    assertEquals(true, tiles.get(0).isSuggested());
    assertEquals(false, tiles.get(1).isSuggested());

    assertEquals(false, melds.get(0).tiles().get(0).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(1).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(2).isSuggested());
    assertEquals(true, melds.get(0).tiles().get(3).isSuggested());
    assertEquals(true, melds.get(0).tiles().get(4).isSuggested());
    assertEquals(true, melds.get(0).tiles().get(5).isSuggested());

    assertEquals(false, melds.get(1).tiles().get(0).isSuggested());
    assertEquals(false, melds.get(1).tiles().get(1).isSuggested());
    assertEquals(false, melds.get(1).tiles().get(2).isSuggested());
  }

  @Test
  public void suggestManipulationRun_test3(){

    List<Tile> tiles = Arrays.asList(O2, R11);
    List<Meld> melds = new ArrayList<>();
    melds.add(Meld.createMeld(O4,O5,O6, JK, O8, O9));
    melds.add(Meld.createMeld(B3, G3, R3, O3));

    PlayerSupporter supporter = new PlayerSupporter(tiles, melds);
    boolean suggestion = supporter.displayHints_1();

    assertEquals(true, suggestion);
    assertEquals(true, tiles.get(0).isSuggested());
    assertEquals(false, tiles.get(1).isSuggested());

//    assertEquals(true, melds.get(0).tiles().get(0).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(1).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(2).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(3).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(4).isSuggested());
    assertEquals(false, melds.get(0).tiles().get(5).isSuggested());

    assertEquals(false, melds.get(1).tiles().get(0).isSuggested());
    assertEquals(false, melds.get(1).tiles().get(1).isSuggested());
    assertEquals(false, melds.get(1).tiles().get(2).isSuggested());
    assertEquals(true, melds.get(1).tiles().get(3).isSuggested());
  }
}
