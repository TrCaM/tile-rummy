package project.rummy.ai;

import org.junit.Before;
import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HandMeldSeekerTest {

    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
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


    @Before
    public void setUp() {  }


    @Test
    public void findPossibleSets_noSetsFound() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3,R11);

        List<Meld> sets = HandMeldSeeker.findPossibleSets(tiles);

        assertEquals(sets.size(), 0);
    }
    @Test
    public void findPossibleSets_test() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3,R11, B11, R8, B5, G11, G5);


        List<Meld> sets = HandMeldSeeker.findPossibleSets(tiles);

        assertEquals(sets.size(), 2);

        assertTrue(sets.get(1).tiles().contains(O5));
        assertTrue(sets.get(1).tiles().contains(B5));
        assertTrue(sets.get(1).tiles().contains(G5));

        assertTrue(sets.get(0).tiles().contains(R11));
        assertTrue(sets.get(0).tiles().contains(B11));
        assertTrue(sets.get(0).tiles().contains(G11));
    }


  @Test
  public void findPossibleSets_withJoker() {
    List<Tile> tiles = Arrays.asList(O5, O10, R3,R11, B11, R8, B5, G11, G5, JK, JK2);

    List<Meld> sets = HandMeldSeeker.findPossibleSets(tiles);

    assertEquals(sets.size(), 2);

    assertEquals(4, sets.get(0).tiles().size());
    assertTrue(sets.get(1).tiles().contains(O5));
    assertTrue(sets.get(1).tiles().contains(B5));
    assertTrue(sets.get(1).tiles().contains(G5));
    assertTrue(sets.get(0).tiles().contains(JK));
//    assertTrue(sets.get(0).tiles().contains(JK2));

    assertEquals(4, sets.get(1).tiles().size());
    assertTrue(sets.get(0).tiles().contains(R11));
    assertTrue(sets.get(0).tiles().contains(B11));
    assertTrue(sets.get(0).tiles().contains(G11));
    //assertTrue(sets.get(1).tiles().contains(JK2));
  }

    @Test
    public void findPossibleRuns_noRunsFound() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3,R11);

        List<Meld> runs = HandMeldSeeker.findPossibleRuns(tiles);

        assertEquals(runs.size(), 0);
    }

    @Test
    public void findPossibleRuns_test() {
        List<Tile> tiles = Arrays.asList(O5,  R3, O8, R8, O7, O6);

        List<Meld> runs = HandMeldSeeker.findPossibleRuns(tiles);

        assertEquals(runs.size(), 1);
        assertTrue(runs.get(0).tiles().contains(O5));
        assertTrue(runs.get(0).tiles().contains(O6));
        assertTrue(runs.get(0).tiles().contains(O7));
        assertTrue(runs.get(0).tiles().contains(O8));
    }
  @Test
  public void findPossibleRuns_withJoker() {
    List<Tile> tiles = Arrays.asList(O5, O10, R3, O8, B11, R8, JK, G11, O6);
    List<Meld> runs = HandMeldSeeker.findPossibleRuns(tiles);

    assertEquals(1, runs.size());
    assertTrue(runs.get(0).tiles().contains(O10));
    assertTrue(runs.get(0).tiles().contains(O8));
    assertTrue(runs.get(0).tiles().contains(JK));
  }

  @Test
  public void findPossibleRuns_with2Jokers() {
    List<Tile> tiles = Arrays.asList(O5, O10, R3, O8, B11, R8, JK, G11, O6, JK2);
    List<Meld> runs = HandMeldSeeker.findPossibleRuns(tiles);

    assertEquals(1, runs.size());
    assertTrue(runs.get(0).tiles().contains(B11));
    assertTrue(runs.get(0).tiles().contains(JK));
    assertTrue(runs.get(0).tiles().contains(JK2));

  }

  @Test
  public void findPossibleRuns_withJoker13() {
    List<Tile> tiles = Arrays.asList(O12, O13, B5, G11, JK);
    List<Meld> runs = HandMeldSeeker.findPossibleRuns(tiles);

    assertEquals(1, runs.size());
    assertTrue(runs.get(0).tiles().contains(JK));
    assertTrue(runs.get(0).tiles().contains(O12));
    assertTrue(runs.get(0).tiles().contains(O13));

  }

    @Test
    public void findPossibleRuns_noMeldsFound() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3,R11);

        List<Meld> sets = HandMeldSeeker.findBestMelds(tiles);

        assertEquals(sets.size(), 0);
    }
    @Test
    public void findPossibleRuns_onlySets() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3,R11, B11, R8, B5, G11, G5);

        List<Meld> melds = HandMeldSeeker.findBestMelds(tiles);

        assertEquals(melds.size(), 2);

        assertTrue(melds.get(1).tiles().contains(O5));
        assertTrue(melds.get(1).tiles().contains(B5));
        assertTrue(melds.get(1).tiles().contains(G5));

        assertTrue(melds.get(0).tiles().contains(R11));
        assertTrue(melds.get(0).tiles().contains(B11));
        assertTrue(melds.get(0).tiles().contains(G11));
    }

    @Test
    public void findPossibleRuns_onlyRuns() {
        List<Tile> tiles = Arrays.asList(O5, O10, R3, O8, B11, R8, O7, G11, O6);


        List<Meld> melds = HandMeldSeeker.findBestMelds(tiles);

        assertEquals(melds.size(), 1);
        assertTrue(melds.get(0).tiles().contains(O5));
        assertTrue(melds.get(0).tiles().contains(O6));
        assertTrue(melds.get(0).tiles().contains(O7));
        assertTrue(melds.get(0).tiles().contains(O8));
    }


    @Test
    public void findBestMelds_test() {
        List<Tile> tiles = Arrays.asList(O5, O10, B5, R3, O8, B11, G5, R8, O7, G11, O6);

        List<Meld> best = HandMeldSeeker.findBestMelds(tiles);

        assertEquals(best.size(), 2);

        assertTrue(best.get(0).tiles().contains(O5));
        assertTrue(best.get(0).tiles().contains(B5));
        assertTrue(best.get(0).tiles().contains(G5));

        assertTrue(best.get(1).tiles().contains(O6));
        assertTrue(best.get(1).tiles().contains(O7));
        assertTrue(best.get(1).tiles().contains(O8));
    }

    @Test
    public void findNextMelds_noMeldsFound() {
        List<Tile> tiles = Arrays.asList(O10, B5, R3, O8, B11, G5, R8, G11, O6);

        Meld next = HandMeldSeeker.findNextMelds(tiles);

        assertTrue(next == null);
    }

    @Test
    public void findNextMelds_onlyRuns() {
        List<Tile> tiles = Arrays.asList(O10, B5, R3, O8, B11, G5, R8, O7, G11, O6);

        //run: O6 O7 O8
        Meld next = HandMeldSeeker.findNextMelds(tiles);

        assertTrue(next.tiles().contains(O6));
        assertTrue(next.tiles().contains(O7));
        assertTrue(next.tiles().contains(O8));
    }


    @Test
    public void findNextMelds_onlySet() {
        List<Tile> tiles = Arrays.asList(O5, O10, B5, R3, O8, B11, G5, R8, O6);

        //set: O5 B5 G5

        Meld next = HandMeldSeeker.findNextMelds(tiles);

        assertTrue(next.tiles().contains(O5));
        assertTrue(next.tiles().contains(B5));
        assertTrue(next.tiles().contains(G5));
    }

    @Test
    public void findNextMelds_test() {
        List<Tile> tiles = Arrays.asList(O5, O10, B5, R3, O8, B11, G5, R8, O7, G11, O6);

        //run: O5 O6 O7 O8
        //set: O5 B5 G5

        Meld next = HandMeldSeeker.findNextMelds(tiles);

        assertTrue(next.tiles().contains(O5));
        assertTrue(next.tiles().contains(O6));
        assertTrue(next.tiles().contains(O7));
        assertTrue(next.tiles().contains(O8));
    }

    @Test
    public void findRemainingTiles_test() {
        List<Tile> tiles = Arrays.asList(O5, O10, B5, R3, O8, B11, G5, R8, O7, G11, O6);

        //run: O5 O6 O7 O8
        //set: O5 B5 G5
        //highest score: O6 O7 O8 AND O5 B5 G5
        //tiles remained: O10 B5 R3 B11 G5 R8 G11

        List<Tile> remain = HandMeldSeeker.findRemainingTiles(tiles);

        assertTrue(remain.contains(O10));
        assertTrue(remain.contains(R3));
        assertTrue(remain.contains(B11));
        assertTrue(remain.contains(R8));
        assertTrue(remain.contains(G11));

    }
}
