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
    private static final Tile  R9 = Tile.createTile(Color.RED, 9);
    private static final Tile R4 = Tile.createTile(Color.RED, 4);
    private static final Tile G4 = Tile.createTile(Color.GREEN, 4);
    private static final Tile B4 = Tile.createTile(Color.BLACK, 4);
    private static final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private static final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private static final Tile R5 = Tile.createTile(Color.RED, 5);
    private static final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private static final Tile B10 = Tile.createTile(Color.BLACK, 10);
  private static final Tile JK = Tile.createTile(Color.ANY, 0);
  private static final Tile JK2 = Tile.createTile(Color.ANY, 0);

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

        assertEquals(0, TableMeldSeeker.findDetachableIdenticalTile(10, Color.BLACK, melds));
        assertEquals(0, TableMeldSeeker.findDetachableIdenticalTile(5, Color.BLACK, melds));
        assertEquals(0, TableMeldSeeker.findDetachableIdenticalTile(7, Color.ORANGE, melds));
        assertEquals(0, TableMeldSeeker.findDetachableIdenticalTile(5, Color.ORANGE, melds));
        assertEquals(melds.get(1).getId(), TableMeldSeeker.findDetachableIdenticalTile(4, Color.GREEN, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(4, Color.ORANGE, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(9, Color.ORANGE, melds));

    }
    @Test
    public void findDetachableIdenticalTile_withJoker(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, JK2));
        melds.add(Meld.createMeld(R4, JK, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

      assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(4, Color.GREEN, melds));
      assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(9, Color.ORANGE, melds));
      assertEquals(melds.get(0).getId(), TableMeldSeeker.findDetachableIdenticalTile(4, Color.ORANGE, melds));

    }



    @Test
    public void findRightDetachableTiles_test(){
        melds.add(Meld.createMeld(O4, O5, O6, O7, O8, O9));
        melds.add(Meld.createMeld(R4, G4, B4, O4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(0, TableMeldSeeker.findRightDetachableTiles(9, Color.ORANGE, melds));
        assertEquals(0, TableMeldSeeker.findRightDetachableTiles(4, Color.RED, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(6, Color.ORANGE, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(7, Color.ORANGE, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findRightDetachableTiles(8, Color.ORANGE, melds));
    }


    @Test
    public void finDirectMeld_test(){
        melds.add(Meld.createMeld(O5, O6, O7, O8));
        melds.add(Meld.createMeld(R4, G4, B4));
        melds.add(Meld.createMeld(G5, B5, O5));

        assertEquals(0, TableMeldSeeker.findDirectMeld(10, Color.BLACK, melds));
        assertEquals(0, TableMeldSeeker.findDirectMeld(4, Color.BLACK, melds));
        assertEquals(0, TableMeldSeeker.findDirectMeld(8, Color.ORANGE, melds));

        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(4, Color.ORANGE, melds));
        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(9, Color.ORANGE, melds));
        assertEquals(melds.get(2).getId(), TableMeldSeeker.findDirectMeld(5, Color.RED, melds));


    }

    @Test
    public void finDirectMeld_test2(){
        melds.add(Meld.createMeld(R5, G5, B5, O5));
        int id = TableMeldSeeker.findDirectMeld(9, Color.RED, melds);
//        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(4, Color.ORANGE, melds));
//        assertEquals(melds.get(0).getId(), TableMeldSeeker.findDirectMeld(9, Color.ORANGE, melds));
        assertEquals(0,id );


    }
}