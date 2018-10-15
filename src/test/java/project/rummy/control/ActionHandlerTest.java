package project.rummy.control;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Table;
import project.rummy.entities.Tile;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.not;
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
  private static final Tile R3 = Tile.createTile(Color.RED, 3);
  private static final Tile G3 = Tile.createTile(Color.GREEN, 3);
  private static final Tile B3 = Tile.createTile(Color.BLACK, 3);

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
  public void playFromHand_shouldSucceed() {
    player.getHand().addTiles(O5, O6, O7, O8, R3, G3, B3);
    player.getHand().formMeld(0, 1, 2);

    handler.playFromHand(0);

    assertThat(player.getHand().getTiles(), not(contains(O5, O6, O7)));
    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(O5, O6, O7));
    assertEquals(player.getHand().getTiles().size(), 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void playFromHand_invalidIndex_shouldThrow() {
    player.getHand().addTiles(O5, O6, O7, O8, R3, G3, B3);
    player.getHand().formMeld(0, 1, 2);

    handler.playFromHand(1);
  }

  @Test(expected =  IllegalAccessException.class)
  public void takeTableMeld_cannotUseTable_shouldThrow() throws IllegalAccessException {
    handler.takeTableMeld(0);
  }

  @Test(expected =  IllegalArgumentException.class)
  public void takeTableMeld_invalidIndex_shouldThrow() throws IllegalAccessException {
    player.setStatus(PlayerStatus.ICE_BROKEN);
    handler = new ActionHandler(player, table);
    Meld meld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld meld2 = Meld.createMeld(R3, G3, B3);
    when(table.getPlayingMelds()).thenReturn(Arrays.asList(meld1, meld2));

    handler.takeTableMeld(3);
  }

  @Test
  public void takeTableMeld_shouldSucceed() throws IllegalAccessException {
    player.setStatus(PlayerStatus.ICE_BROKEN);
    handler = new ActionHandler(player, table);
    Meld meld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld meld2 = Meld.createMeld(R3, G3, B3);
    when(table.getPlayingMelds()).thenReturn(Arrays.asList(meld1, meld2));
    when(table.removeMeld(1)).thenReturn(meld2);

    handler.takeTableMeld(1);

    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(R3, G3, B3));
    verify(table).getPlayingMelds();
    verify(table).removeMeld(1);
  }
}