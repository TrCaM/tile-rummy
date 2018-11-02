package project.rummy.ai;

import org.junit.Before;
import org.junit.Test;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TableMeldSeekerTest {

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
    private static final Tile R5 = Tile.createTile(Color.RED, 5);
    private static final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private static final Tile B10 = Tile.createTile(Color.BLACK, 10);

    private List<Meld> melds;

    @Before
    public void setUp() {
        melds = new ArrayList<>();
    }

    @Test
    public void findDetachableIdenticalTile_test(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertTrue(TableMeldSeeker.findDetachableIdenticalTile(B10, melds) == null);
        assertTrue(TableMeldSeeker.findDetachableIdenticalTile(B5, melds) == null);
        assertTrue(TableMeldSeeker.findDetachableIdenticalTile(O7, melds) == null);
        assertTrue(TableMeldSeeker.findDetachableIdenticalTile(O5, melds) == null);
        assertEquals(melds.get(1).getId(), TableMeldSeeker.findDetachableIdenticalTile(G4, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(O4, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(O9, melds).getId());

    }


    @Test
    public void findLeftDetachableTiles_test(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertTrue(TableMeldSeeker.findLeftDetachableTiles(O8, melds) == null);
        assertTrue (TableMeldSeeker.findLeftDetachableTiles(G4, melds) == null);
        assertTrue(TableMeldSeeker.findLeftDetachableTiles(O4, melds) == null);
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findLeftDetachableTiles(O6, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findLeftDetachableTiles(O7, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findLeftDetachableTiles(O5, melds).getId());
    }


    @Test
    public void findRightDetachableTiles_test(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(null, TableMeldSeeker.findRightDetachableTiles(O9, melds));
        assertEquals(null, TableMeldSeeker.findRightDetachableTiles(R4, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(O8, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(O7, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(O6, melds).getId());
    }


    @Test
    public void finDirectMeld_test(){
        melds.add(Meld.createMeld(O5, O6, O7, O8));
        melds.add(Meld.createMeld(R4, G4, B4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(null, TableMeldSeeker.findDirectMeld(B10, melds));
        assertEquals(null, TableMeldSeeker.findDirectMeld(B4, melds));
        assertEquals(null, TableMeldSeeker.findDirectMeld(O8, melds));

        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(O4, melds).getId());
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(O9, melds).getId());
        assertEquals(melds.get(2).getId(), TableMeldSeeker.findDirectMeld(R5, melds).getId());


    }
}