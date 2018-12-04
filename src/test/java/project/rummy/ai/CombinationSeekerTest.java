package project.rummy.ai;

import org.junit.Before;
import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.*;

import static org.junit.Assert.*;

public class CombinationSeekerTest {


    private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
    private static final Tile O10 = Tile.createTile(Color.ORANGE, 10);
    private static final Tile R4 = Tile.createTile(Color.RED, 4);
    private static final Tile G4 = Tile.createTile(Color.GREEN, 4);
    private static final Tile B4 = Tile.createTile(Color.BLACK, 4);
    private static final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private static final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private static final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private static final Tile R5 = Tile.createTile(Color.RED, 5);

    private static final Tile R10 = Tile.createTile(Color.RED, 10);

    private static final Tile R11 = Tile.createTile(Color.RED, 11);
    private static final Tile G11 = Tile.createTile(Color.GREEN, 11);
    private static final Tile B11 = Tile.createTile(Color.BLACK, 11);
    private static final Tile O11 = Tile.createTile(Color.ORANGE, 11);
    private static final Tile JK = Tile.createTile(Color.ANY, 0);
    private static final Tile JK2 = Tile.createTile(Color.ANY, 0);

    private static final Tile R13 = Tile.createTile(Color.RED, 13);
    private static final Tile G13 = Tile.createTile(Color.GREEN, 13);
    private static final Tile B13 = Tile.createTile(Color.BLACK, 13);
    private static final Tile O13 = Tile.createTile(Color.ORANGE, 13);

    private static final Tile R6 = Tile.createTile(Color.RED, 6);
    private static final Tile G6 = Tile.createTile(Color.GREEN, 6);
    private static final Tile B6 = Tile.createTile(Color.BLACK, 6);




    private List<Meld> melds;
    private List<Tile> tiles;

    @Before
    public void setUp() {
        melds = new ArrayList<>();
        tiles = new ArrayList<>();

    }

    @Test
    public void formSet_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        List<Tile> t = new ArrayList<>();
        t.add(R4);
        Map<Meld, Integer> map = new CombinationSeeker(tiles,melds).formSet(t);

        assertEquals(2,map.size());
        assertTrue(map.containsKey(melds.get(0)));
        assertTrue(map.containsKey(melds.get(1)));
        //it will look through the tile with same value 4
        //and search for color in alphabetical order: B -> G -> O -> R
        //with B4: meld [R4, G4, B4, O4] is found and index of the tile is 2
        //with G4: None (since meld [R4, G4, B4, O4] cannot appear a second time)
        //with O4: meld [O4, O5, O6, O7, O8, O9] is found and index of the tile is 0
        assertTrue(map.get(melds.get(1))==2);
        assertTrue(map.get(melds.get(0))==0);


        List<Tile> t2 = new ArrayList<>();
        t2.add(R10);
        Map<Meld, Integer> map2 = new CombinationSeeker(tiles,melds).formSet(t2);
        assertEquals(0,map2.size());

        List<Tile> t3 = new ArrayList<>();
        t3.add(O5);
        Map<Meld, Integer> map3 = new CombinationSeeker(tiles,melds).formSet(t3);
        assertEquals(0, map3.size());
    }
    @Test
    public void formRunByDetaching_test2(){
        melds.add(Meld.createMeld(R6,O6,B6,G6));
        melds.add(Meld.createMeld(R13,G13,O13,B13));

        List<Tile> t = new ArrayList<>();
        t.add(R5);
        Map<Meld, Integer> map = new CombinationSeeker(tiles,melds).formRunByDetaching(t);
        assertEquals(1,map.size());
    }

    @Test
    public void formSet_withJoker() {
        melds.add(Meld.createMeld(JK, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5, JK2));

        List<Tile> t = new ArrayList<>();
        t.add(R4);

        Map<Meld, Integer> map = new CombinationSeeker(tiles,melds).formSet(t);
    assertEquals(3,map.size());
    assertEquals(map.get(melds.get(1)).intValue(), 1);
    assertEquals(map.get(melds.get(0)).intValue(), 5);
    assertEquals(map.get(melds.get(2)).intValue(), 3);
}


    @Test
    public void formRunByDetaching_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(R11, G11, B11, O11));
        melds.add(Meld.createMeld(G5, B5, O5));

        List<Tile> t = new ArrayList<>();
        t.add(O10);

        Map<Meld, Integer> map = new CombinationSeeker(tiles,melds).formRunByDetaching(t);

        assertEquals(2,map.size());
        assertTrue(map.containsKey(melds.get(0)));
        assertTrue(map.containsKey(melds.get(2)));
        assertEquals(3, (int) map.get(melds.get(2)));
        assertEquals(5, (int) map.get(melds.get(0)));

        List<Tile> t1 = new ArrayList<>();
        t1.add(R10);

        Map<Meld, Integer> map2 = new CombinationSeeker(t1,melds).formRunByDetaching(t1);
        assertEquals(1,map2.size());
        assertEquals(0, (int) map2.get(melds.get(2)));


        List<Tile> t2 = new ArrayList<>();
        t2.add(O5);
        Map<Meld, Integer> map3 = new CombinationSeeker(t2,melds).formRunByDetaching(t2);
        assertEquals(1, map3.size());
        assertTrue(map3.get(melds.get(0))==0);
    }


    @Test
    public void formRunBySplitRight_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(R11, G11, B11, O11));
        melds.add(Meld.createMeld(G5, B5, O5));

        Map<Meld, Integer> map = new CombinationSeeker(tiles,melds).formRunBySplitRight(10, Color.ORANGE);

        assertEquals(0,map.size());

        Map<Meld, Integer> map2 = new CombinationSeeker(tiles,melds).formRunBySplitRight(7, Color.ORANGE);
        assertEquals(1,map2.size());
        assertTrue(map2.get(melds.get(0))==3);

        Map<Meld, Integer> map3 = new CombinationSeeker(tiles,melds).formRunBySplitRight(5, Color.ORANGE);
        assertEquals(0, map3.size());

        Map<Meld, Integer> map4 = new CombinationSeeker(tiles,melds).formRunBySplitRight(8, Color.ORANGE);
        assertEquals(0, map4.size());
    }

}