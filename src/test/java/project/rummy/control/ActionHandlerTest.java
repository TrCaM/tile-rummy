package project.rummy.control;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.entities.Color;
import project.rummy.entities.Table;
import project.rummy.entities.Tile;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionHandlerTest {

  private static final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private static final Tile O6 = Tile.createTile(Color.ORANGE, 6);
  private static final Tile O7 = Tile.createTile(Color.ORANGE, 7);
  private static final Tile O8 = Tile.createTile(Color.ORANGE, 8);
  private static final Tile O9 = Tile.createTile(Color.ORANGE, 9);
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);
  private static final Tile O3 = Tile.createTile(Color.ORANGE, 3);

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private Table table;

  private Player player = new Player();

  private ActionHandler handler;

  @Before
  public void setUp() {
    handler = new ActionHandler(player, table);
  }

  @Test
  public void draw_shouldSucceed() {
    when(table.drawTile()).thenReturn(O5).thenReturn(R3);

    handler.draw();
    handler.draw();

    assertThat(player.getHand().getTiles(), contains(O5, R3));
    verify(table, times(2)).drawTile();
  }

  @Test
  public void playFromHand() {
  }

  @Test
  public void manipulateMeld() {
  }

  @Test
  public void endTurn() {
  }
}