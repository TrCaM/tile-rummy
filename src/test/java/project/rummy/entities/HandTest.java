package project.rummy.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.*;

public class HandTest {
  private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile O10 = Tile.createTile(Color.ORANGE, 10);
  private final Tile R3 = Tile.createTile(Color.RED, 3);
  private final Tile R8 = Tile.createTile(Color.RED, 8);
  private final Tile B13 = Tile.createTile(Color.BLACK, 13);
  private final Tile B7 = Tile.createTile(Color.BLACK, 7);
  private final Tile B5 = Tile.createTile(Color.BLACK, 5);
  private final Tile B1 = Tile.createTile(Color.BLACK, 1);
  private final Tile G11 = Tile.createTile(Color.GREEN, 11);
  private final Tile G5 = Tile.createTile(Color.GREEN, 5);
  private Hand hand;

  @Before
  public void setUp() {
  }

  @Test
  public void addTile_shouldSucceed() {
    hand = new Hand();
    hand.addTile(O5);

    assertThat(hand.getTiles(), hasItem(O5));
  }

  @Test
  public void getScore_emptyHand() {
    hand = new Hand();
    assertEquals(hand.getScore(), 0);
    //assertThat(hand.getScore(), is(0));
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
}