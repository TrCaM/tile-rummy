package project.rummy.entities;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TileTest {

  @Test
  public void createTile_numberTile_shouldSucceed() {
    Tile tile = Tile.createTile(Color.BLACK, 10);

    assertEquals(tile.color, Color.BLACK);
    assertEquals(tile.value, 10);
    assertEquals(tile.getClass(), NumberTile.class);
  }

  @Test
  public void createTile_jokerTile_shouldSucceed() {
    Tile tile = Tile.createTile(Color.ORANGE, 0);

    assertEquals(tile.color, Color.ANY);
    assertEquals(tile.value, 30);
    assertEquals(tile.getClass(), Joker.class);
  }
}