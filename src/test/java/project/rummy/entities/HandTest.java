package project.rummy.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HandTest {
  private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile O10 = Tile.createTile(Color.ORANGE, 10);
  private final Tile R3 = Tile.createTile(Color.RED, 3);
  private final Tile R8 = Tile.createTile(Color.RED, 8);
  private final Tile B13 = Tile.createTile(Color.BLACK, 13);
  private final Tile B1 = Tile.createTile(Color.BLACK, 1);
  private final Tile G11 = Tile.createTile(Color.GREEN, 11);
  private Hand hand;

  @Before
  public void setUp() throws Exception {
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
}