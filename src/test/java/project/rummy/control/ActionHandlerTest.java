package project.rummy.control;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.entities.*;

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

  @Mock
  private Player player;

  private ActionHandler handler;

  private Hand hand = new Hand();

  @Before
  public void setUp() {
    when(player.hand()).thenReturn(hand);
    when(player.status()).thenReturn(PlayerStatus.START).thenReturn(PlayerStatus.ICE_BROKEN);
    handler = new ActionHandler(player, table);
  }

  @Test
  public void draw_shouldSucceed() {
    when(table.drawTile()).thenReturn(O5).thenReturn(R3);

    handler.drawAndEndTurn();
    handler.drawAndEndTurn();

    assertThat(player.hand().getTiles(), contains(R3, O5));
    verify(table, times(2)).drawTile();
  }

  @Test
  public void timeout_shouldSucceed() {
    when(table.drawTile()).thenReturn(O5).thenReturn(R3).thenReturn(G3);

    handler.getManipulationTable().add(Meld.createMeld(R3));
    handler.timeOutEndTurn();

    assertThat(player.hand().getTiles(), contains(R3, G3, O5));
    verify(table, times(3)).drawTile();
  }

  @Test
  public void playFromHand_shouldSucceed() {
    player.hand().addTiles(O5, O6, O7, O8, R3, G3, B3);
    player.hand().formMeld(0, 1, 2);

    handler.playFromHand(0);

    assertThat(player.hand().getTiles(), not(contains(O5, O6, O7)));
    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(O5, O6, O7));
    assertEquals(player.hand().getTiles().size(), 4);
  }

  @Test
  public void playFromHand_byMeld_shouldSucceed() {
    player.hand().addTiles(O5, O6, O7, O8, R3, G3, B3);
    Meld meld = player.hand().formMeld(0, 1, 2).get(0);

    handler.playFromHand(meld);

    assertThat(player.hand().getTiles(), not(contains(O5, O6, O7)));
    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(O5, O6, O7));
    assertEquals(player.hand().getTiles().size(), 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void playFromHand_invalidIndex_shouldThrow() {
    player.hand().addTiles(O5, O6, O7, O8, R3, G3, B3);
    player.hand().formMeld(0, 1, 2);

    handler.playFromHand(1);
  }

  @Test(expected =  IllegalStateException.class)
  public void takeTableMeld_cannotUseTable_shouldThrow(){
    handler.takeTableMeld(0);
  }

  @Test(expected =  IllegalArgumentException.class)
  public void takeTableMeld_invalidIndex_shouldThrow(){
    player.setStatus(PlayerStatus.ICE_BROKEN);
    handler = new ActionHandler(player, table);
    Meld meld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld meld2 = Meld.createMeld(R3, G3, B3);
    when(table.getPlayingMelds()).thenReturn(Arrays.asList(meld1, meld2));

    handler.takeTableMeld(3);
  }


  @Test
  public void takeTableMeld_shouldSucceed(){
    player.setStatus(PlayerStatus.ICE_BROKEN);
    handler = new ActionHandler(player, table);
    Meld meld1 = Meld.createMeld(O5, O6, O7, O8);
    Meld meld2 = Meld.createMeld(R3, G3, B3);
    when(table.getPlayingMelds()).thenReturn(Arrays.asList(meld1, meld2));
    when(table.removeMeld(1)).thenReturn(meld2);

    handler.takeTableMeld(1);

    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(R3, G3, B3));
    verify(table).removeMeld(1);
  }

  @Test
  public void takeHandTile_shouldSucceed()  {
    player.setStatus(PlayerStatus.START);
    handler = new ActionHandler(player, table);
    hand.addTiles(R3, O5, O8);


    handler.takeHandTile(1);

    assertThat(handler.getManipulationTable().getMelds().get(0).tiles(), contains(O5));
    assertEquals(handler.getHand().getTiles().size(), 2);
    assertTrue(handler.getHand().getTiles().contains(R3));
    assertTrue(handler.getHand().getTiles().contains(O8));
    assertFalse(handler.getHand().getTiles().contains(O5));

  }

  @Test
  public void formMeld_notAbleToForm_shouldForm2SingleMelds() {
    player.setStatus(PlayerStatus.START);
    handler = new ActionHandler(player, table);
    hand.addTiles(R3, O5, O8);


    handler.formMeld(1, 2);

    assertEquals(handler.getHand().getTiles().size(), 1);
    assertTrue(handler.getHand().getMelds().get(0).tiles().contains(O5));
    assertTrue(handler.getHand().getMelds().get(1).tiles().contains(O8));
  }
}
