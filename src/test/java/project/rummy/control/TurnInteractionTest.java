package project.rummy.control;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.entities.*;
import project.rummy.entities.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class TurnInteractionTest {
    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);
    private final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile G8 = Tile.createTile(Color.GREEN, 8);
    private final Tile B8 = Tile.createTile(Color.BLACK, 8);
    private final Tile R8 = Tile.createTile(Color.RED, 8);




  //private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
  private final Tile G5 = Tile.createTile(Color.GREEN, 5);
  private final Tile B5 = Tile.createTile(Color.BLACK, 5);
  private final Tile R5 = Tile.createTile(Color.RED, 5);
  private final Tile B4 = Tile.createTile(Color.BLACK, 4);
  private final Tile B6 = Tile.createTile(Color.BLACK, 6);
  private final Tile B7 = Tile.createTile(Color.BLACK, 7);

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private Controller controller;

  private Player player;

  @Before
  public void setUp() {
    player = new Player(controller);
  }

  @Test
  public void scenario_1_Test() throws IllegalAccessException {
    /* Set up controllers and hand */
    player.setStatus(PlayerStatus.ICE_BROKEN);
    Hand hand = player.hand();
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
    assertThat(hand.getMelds().size(), is( 0));
    assertThat(hand.getTiles().size(), is(0));
  }
  @Test
  //Add on -- Adding tiles from hand to from new meld on the table
  public void scenario_2_Test() throws IllegalAccessException{
      //Set up controllers and hand
      player.setStatus(PlayerStatus.ICE_BROKEN);
      Hand hand = player.hand();
      hand.addTiles(O3, O8);

      // Set up the table
      Table table = new Table();
      Meld setOf6 =  Meld.createMeld(O4, O5, O6, R8, G8, B8 );
      table.addMeld(setOf6);

      //Create action handler
      ActionHandler handler = new ActionHandler(player, table);
      ManipulationTable tempTable = handler.getManipulationTable();

      //Test table before turn
      assertThat(tempTable.getMelds().isEmpty(), is (true));

      /* Start the turn  */
      //Adding O4, O5, O6, R8, G8, B8 to tempt table
      handler.takeTableMeld(0);
      //Forming tiles to play from hand  {O3} , {O8}
      hand.formMeld(0);
      hand.formMeld(0);
      //Adding {O3} , {O8} to manipulation table
      handler.playFromHand(0);
      handler.playFromHand(0);
      //Combine {O3} to O4...O6 and {O8} to R8...B8
      tempTable.combineMelds(0,2);
      tempTable.combineMelds(1,3);
      //End turn
      handler.endTurn();

      //Checking the turn
      assertThat(table.getPlayingMelds().size(),is(2) );
      assertThat(table.getPlayingMelds().get(0).tiles(),contains(O3, O4, O5, O6));
      assertThat(table.getPlayingMelds().get(1).tiles(),contains(R8, B8, G8, O8));
      assertThat(hand.getMelds().size(), is( 0));
      assertThat(hand.getTiles().size(), is(0));
  }
}

