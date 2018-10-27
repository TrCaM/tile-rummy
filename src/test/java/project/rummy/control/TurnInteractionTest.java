package project.rummy.control;

import org.junit.Test;
import project.rummy.entities.*;
import project.rummy.player.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class TurnInteractionTest {
  private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile G5 = Tile.createTile(Color.GREEN, 5);
  private final Tile B5 = Tile.createTile(Color.BLACK, 5);
  private final Tile R5 = Tile.createTile(Color.RED, 5);
  private final Tile B4 = Tile.createTile(Color.BLACK, 4);
  private final Tile B6 = Tile.createTile(Color.BLACK, 6);
  private final Tile B7 = Tile.createTile(Color.BLACK, 7);

  @Test
  public void scenario_1_Test() throws IllegalAccessException {
    /* Set up player and hand */
    Player player = new Player();
    player.setStatus(PlayerStatus.ICE_BROKEN);
    Hand hand = player.getHand();
    hand.addTiles(B4, B6, B7);
    /* Set up the table before the turn happens */
    Table table = new Table();
    Meld setOf5 = Meld.createMeld(O5, G5, B5, R5);
    table.addMeld(setOf5);
    /* Create action handler */
    ActionHandler handler = new ActionHandler(player, table);
    ManipulationTable tempTable = handler.getManipulationTable();

    /* Test the situation before the turn start */
    assertThat(tempTable.getMelds().isEmpty(), is(true));

    /* Start to executed the turn */
    // Step 1: Take the meld 5 5 5 5 to manipulation table
    handler.takeTableMeld(0);
    // Step 2: Form meld for tiles in the hand: {B4} and {B6, B7}
    hand.formMeld(0);
    hand.formMeld(0, 1);
    // Step 3: Add meld to the manipulation table
    // Note the index of the meld is all 0 here
    handler.playFromHand(0);
    handler.playFromHand(0);
    // Step 4: Detach B5 from setOf5
    tempTable.detach(0, 2);
    // Step 5: Combine into {B4.. B7}
    tempTable.combineMelds(0, 1, 2);
    // Now finish the turn by submitting
    handler.endTurn();

    /* Now we check the the state of the game is as expected */
    assertThat(table.getPlayingMelds().size(), is(2));
    assertThat(table.getPlayingMelds().get(0).tiles(), contains(O5, G5, R5));
    assertThat(table.getPlayingMelds().get(1).tiles(), contains(B4, B5, B6, B7));
    assertThat(hand.getMelds().size(), is(0));
    assertThat(hand.getTiles().size(), is(0));

  }
}
