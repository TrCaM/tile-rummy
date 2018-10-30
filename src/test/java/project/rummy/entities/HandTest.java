package project.rummy.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

public class HandTest {
  private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile O10 = Tile.createTile(Color.ORANGE, 10);
  private final Tile R3 = Tile.createTile(Color.RED, 3);
  private final Tile R8 = Tile.createTile(Color.RED, 8);
  private final Tile B13 = Tile.createTile(Color.BLACK, 13);
  private final Tile B7 = Tile.createTile(Color.BLACK, 7);
  private final Tile B6 = Tile.createTile(Color.BLACK, 6);
  private final Tile B5 = Tile.createTile(Color.BLACK, 5);
  private final Tile B1 = Tile.createTile(Color.BLACK, 1);
  private final Tile G11 = Tile.createTile(Color.GREEN, 11);
  private final Tile G5 = Tile.createTile(Color.GREEN, 5);
  private Hand hand;

  @Before
  public void setUp() {
  }

  @Test(expected = IllegalArgumentException.class)
   public void addTileToMeld_invalidIndex() {
       hand = new Hand();
       hand.addTiles(O5, O10, G5, B7, B5);
       hand.formMeld(0,2);

       hand.addTileToMeld(0,1);
   }

   @Test(expected = IllegalArgumentException.class)
   public void addTileToMeld_invalidMeld() {
       hand = new Hand();
       hand.addTiles(O5, O10, G5, B7, B5);
       hand.formMeld(0,2);

       hand.addTileToMeld(0,0);
   }

   @Test
   public void addTileToMeld_formNewSet() {
       hand = new Hand();
       hand.addTiles(O5, O10, G5, B7, B5);
       hand.formMeld(0,2);

       hand.addTileToMeld(2,0);

       assertEquals(hand.getTiles().size(), 2);
       assertEquals(hand.getMelds().get(0).tiles().size(), 3);
       assertTrue(hand.getMelds().get(0).tiles().contains(B5));
       assertTrue(hand.getMelds().get(0).tiles().contains(O5));
       assertTrue(hand.getMelds().get(0).tiles().contains(G5));
   }

   @Test
   public void addTileToMeld_formNewRun() {
       hand = new Hand();
       hand.addTiles(O5,B6, O10, G5, B7, B5);
       hand.formMeld(1,5);

       hand.addTileToMeld(3,0);

       assertEquals(hand.getTiles().size(), 3);
       assertEquals(hand.getMelds().get(0).tiles().size(), 3);
       assertTrue(hand.getMelds().get(0).tiles().contains(B5));
       assertTrue(hand.getMelds().get(0).tiles().contains(B6));
       assertTrue(hand.getMelds().get(0).tiles().contains(B7));
   }

  @Test
  public void addTile_shouldSucceed() {
    hand = new Hand();
    hand.addTile(O5);

    assertThat(hand.getTiles(), hasItem(O5));
  }

  @Test
  public void addTiles_shouldSucceed() {
    hand = new Hand();
    hand.addTiles(O5, R3, R8, B13);

    assertThat(hand.getTiles(), contains(O5, R3, R8, B13));
  }

  @Test
  public void getScore_emptyHand() {
    hand = new Hand();
    assertEquals(hand.getScore(), 0);
  }

  @Test
  public void getScore_shouldReturnCorrectScore() {
    List<Tile> tiles = Arrays.asList(O5, O10, R3, R8);
    hand = new Hand(tiles);

    assertEquals(hand.getScore(), 26);
  }

  @Test
  public void getScore_v2_shouldReturnCorrectScore() {
    List<Tile> tiles = Arrays.asList(B13, B1, O10);
    hand = new Hand(tiles);

    assertEquals(hand.getScore(), 24);
  }

  @Test
  public void sort_sameColor() {
    List<Tile> tiles = Arrays.asList(B13, B1, B7);
    hand = new Hand(tiles);
    hand.sort();

    Tile t0 = hand.getTiles().get(0);
    Tile t1 = hand.getTiles().get(1);
    Tile t2 = hand.getTiles().get(2);

    //it should appear in order of increasing values
    //order in this case: B1, B7, B13

    assertTrue(t0.value()==B1.value() && t0.color().equals(B1.color()));
    assertTrue(t1.value()==B7.value() && t1.color().equals(B7.color()));
    assertTrue(t2.value()==B13.value() && t2.color().equals(B13.color()));
  }

  @Test
  public void sort_sameValue() {
    List<Tile> tiles = Arrays.asList(B5, O5, G5);
    hand = new Hand(tiles);
    hand.sort();

    Tile t0 = hand.getTiles().get(0);
    Tile t1 = hand.getTiles().get(1);
    Tile t2 = hand.getTiles().get(2);

    //order should be: B5, G5, O5

    assertTrue(t0.value()==B5.value() && t0.color().equals(B5.color()));
    assertTrue(t1.value()==G5.value() && t1.color().equals(G5.color()));
    assertTrue(t2.value()==O5.value() && t2.color().equals(O5.color()));
  }

  @Test
  public void sort_random() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3);
    hand = new Hand(tiles);
    hand.sort();

    Tile t0 = hand.getTiles().get(0);
    Tile t1 = hand.getTiles().get(1);
    Tile t2 = hand.getTiles().get(2);
    Tile t3 = hand.getTiles().get(3);
    Tile t4 = hand.getTiles().get(4);

    //red->black->green->orange
    //order in this case: R3, B7, B13, G11, O5

    assertTrue(t0.value()==R3.value() && t0.color().equals(R3.color()));
    assertTrue(t1.value()==B7.value() && t1.color().equals(B7.color()));
    assertTrue(t2.value()==B13.value() && t2.color().equals(B13.color()));
    assertTrue(t3.value()==G11.value() && t3.color().equals(G11.color()));
    assertTrue(t4.value()==O5.value() && t4.color().equals(O5.color()));
  }

  @Test
  public void formMeld_shouldSucceed() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3, B5, G5);
    hand = new Hand(tiles);

    hand.formMeld(1, 5);

    assertThat(hand.getMelds().get(0).tiles(), contains(O5, B5));
    assertEquals(hand.getTiles().size(), 5);
    assertThat(hand.getTiles(), not(contains(O5, B5)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void formMeld_invalidIndex_shouldThrow() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3, B5, G5);
    hand = new Hand(tiles);

    hand.formMeld(1, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void formMeld_invalidMeld_shouldThrow() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3, B5, G5);
    hand = new Hand(tiles);

    hand.formMeld(1, 0);
  }

  @Test
  public void removeMeld_shouldSucceed() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3, B5, G5);
    hand = new Hand(tiles);
    hand.formMeld(1, 6);
    hand.formMeld(0);

    Meld expectedMeld = hand.removeMeld(1);

    assertThat(expectedMeld.tiles(), contains(B13));
    assertEquals(hand.getMelds().size(), 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeMeld_invalidIndex_shouldThrow() {
    List<Tile> tiles = Arrays.asList(B13, O5, B7, G11, R3, B5, G5);
    hand = new Hand(tiles);
    hand.formMeld(1, 6);
    hand.formMeld(0);

    Meld expectedMeld = hand.removeMeld(3);
  }
}