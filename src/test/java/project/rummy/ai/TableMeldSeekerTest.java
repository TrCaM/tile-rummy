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
    public void findDetachableMeld_test(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(0, TableMeldSeeker.findDetachableMeld(B10, melds, DetachDirection.NONE));
        assertEquals(0, TableMeldSeeker.findDetachableMeld(B5, melds, DetachDirection.NONE));
        assertEquals(0, TableMeldSeeker.findDetachableMeld(O7, melds, DetachDirection.LEFT));
        assertEquals(0, TableMeldSeeker.findDetachableMeld(O7, melds, DetachDirection.NONE));
        assertEquals(0, TableMeldSeeker.findDetachableMeld(O5, melds, DetachDirection.NONE));
        assertEquals(0, TableMeldSeeker.findDetachableMeld(G4, melds, DetachDirection.LEFT));

        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableMeld(O6, melds, DetachDirection.LEFT));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableMeld(O5, melds, DetachDirection.LEFT));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableMeld(O9, melds, DetachDirection.RIGHT));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableMeld(O8, melds, DetachDirection.RIGHT));
        assertEquals(melds.get(1).getId(), TableMeldSeeker.findDetachableMeld(G4, melds, DetachDirection.NONE));

        assertEquals(melds.get(1).getId(), TableMeldSeeker.findDetachableMeld(O4, melds, DetachDirection.NONE));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableMeld(O4, melds, DetachDirection.LEFT));
    }


    @Test
    public void finDirectMeldToAdd_test(){
        melds.add(Meld.createMeld(O5, O6, O7, O8));
        melds.add(Meld.createMeld(R4, G4, B4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(0, TableMeldSeeker.findDirectMeldToAdd(B10, melds));
        assertEquals(0, TableMeldSeeker.findDirectMeldToAdd(B4, melds));
        assertEquals(0, TableMeldSeeker.findDirectMeldToAdd(O8, melds));

        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeldToAdd(O4, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeldToAdd(O9, melds));
        assertEquals(melds.get(2).getId(), TableMeldSeeker.findDirectMeldToAdd(R5, melds));


    }
}