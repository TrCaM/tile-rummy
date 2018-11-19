package project.rummy.ai;

import org.junit.Before;
import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CombinationSeekerTest {


    private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
    private static final Tile R4 = Tile.createTile(Color.RED, 4);
    private static final Tile G4 = Tile.createTile(Color.GREEN, 4);
    private static final Tile B4 = Tile.createTile(Color.BLACK, 4);
    private static final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private static final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private static final Tile B5 = Tile.createTile(Color.BLACK, 5);

    private static final Tile R10 = Tile.createTile(Color.RED, 10);

    private static final Tile R11 = Tile.createTile(Color.RED, 11);
    private static final Tile G11 = Tile.createTile(Color.GREEN, 11);
    private static final Tile B11 = Tile.createTile(Color.BLACK, 11);
    private static final Tile O11 = Tile.createTile(Color.ORANGE, 11);


    private List<Meld> melds;

    @Before
    public void setUp() {
        melds = new ArrayList<>();
    }

    @Test
    public void formSet_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        List<Tile> t = new ArrayList<>();
        t.add(R4);
        Map<Meld, Integer> map = CombinationSeeker.formSet(t, melds);

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
        Map<Meld, Integer> map2 = CombinationSeeker.formSet(t2, melds);
        assertEquals(0,map2.size());

        List<Tile> t3 = new ArrayList<>();
        t3.add(O5);
        Map<Meld, Integer> map3 = CombinationSeeker.formSet(t3, melds);
        assertEquals(0, map3.size());
    }


    @Test
    public void formRunByDetaching_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(R11, G11, B11, O11));
        melds.add(Meld.createMeld(G5, B5, O5));

        Map<Meld, Integer> map = CombinationSeeker.formRunByDetaching(10, Color.ORANGE, melds);

        assertEquals(2,map.size());
        assertTrue(map.containsKey(melds.get(0)));
        assertTrue(map.containsKey(melds.get(2)));
        assertEquals(3, (int) map.get(melds.get(2)));
        assertEquals(5, (int) map.get(melds.get(0)));

        Map<Meld, Integer> map2 = CombinationSeeker.formRunByDetaching(10, Color.RED, melds);
        assertEquals(1,map2.size());
        assertEquals(0, (int) map2.get(melds.get(2)));

        Map<Meld, Integer> map3 = CombinationSeeker.formRunByDetaching(5, Color.ORANGE, melds);
        assertEquals(1, map3.size());
        assertTrue(map3.get(melds.get(0))==0);
    }


    @Test
    public void formRunBySplitRight_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(R11, G11, B11, O11));
        melds.add(Meld.createMeld(G5, B5, O5));

        Map<Meld, Integer> map = CombinationSeeker.formRunBySplitRight(10, Color.ORANGE, melds);

        assertEquals(0,map.size());

        Map<Meld, Integer> map2 = CombinationSeeker.formRunBySplitRight(7, Color.ORANGE, melds);
        assertEquals(1,map2.size());
        assertTrue(map2.get(melds.get(0))==3);

        Map<Meld, Integer> map3 = CombinationSeeker.formRunBySplitRight(5, Color.ORANGE, melds);
        assertEquals(0, map3.size());

        Map<Meld, Integer> map4 = CombinationSeeker.formRunBySplitRight(8, Color.ORANGE, melds);
        assertEquals(0, map4.size());
    }


    @Test
    public void formRunBySplitLeft_test() {
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(R11, G11, B11, O11));
        melds.add(Meld.createMeld(G5, B5, O5));

        Map<Meld, Integer> map = CombinationSeeker.formRunBySplitLeft(8, Color.ORANGE, melds);
        assertEquals(0,map.size());

        Map<Meld, Integer> map2 = CombinationSeeker.formRunBySplitLeft(7, Color.ORANGE, melds);
        assertEquals(1,map2.size());
        assertTrue(map2.get(melds.get(0))==3);

        Map<Meld, Integer> map3 = CombinationSeeker.formRunBySplitLeft(5, Color.ORANGE, melds);
        assertEquals(0, map3.size());
    }
}