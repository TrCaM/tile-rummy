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
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private final Tile G6 = Tile.createTile(Color.GREEN, 6);
    private final Tile G7 = Tile.createTile(Color.GREEN, 7);
    private final Tile G8 = Tile.createTile(Color.GREEN, 8);
    private final Tile B6 = Tile.createTile(Color.BLACK, 6);
    private final Tile B7 = Tile.createTile(Color.BLACK, 7);
    private final Tile B8 = Tile.createTile(Color.BLACK, 8);
    private final Tile R8 = Tile.createTile(Color.RED, 8);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Controller controller;

    private Player player;

    @Before
    public void setUp() {
        player = new Player("Player", controller, 0);

    }

    @Test
    //Add on -- Adding tiles from hand to from new meld on the table
    public void scenario_1_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(O3, O8);

        // Set up the table
        Table table = new Table();
        Meld setOfRun = Meld.createMeld(O4, O5, O6);
        Meld setOfSet = Meld.createMeld(R8, G8, B8);
        table.addMeld(setOfRun);
        table.addMeld(setOfSet);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        /* Start the turn  */
        //Adding {O4, O5, O6} and {R8, G8, B8}   to tempt table
        handler.takeTableMeld(0);
        handler.takeTableMeld(0);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O4, O5, O6));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(R8, G8, B8));

        //Forming tiles {O3} , {O8} to play from hand
        hand.formMeld(0);
        hand.formMeld(0);
        assertThat(hand.getMelds().get(0).tiles(), contains(O3));
        assertThat(hand.getMelds().get(1).tiles(), contains(O8));

        //Adding {O3} , {O8} to manipulation table
        handler.playFromHand(0);
        handler.playFromHand(0);
        assertThat(tempTable.getMelds().get(2).tiles(), contains(O3));
        assertThat(tempTable.getMelds().get(3).tiles(), contains(O8));

        //Combine {O3} to O4...O6 and {O8} to R8...B8
        tempTable.combineMelds(0, 2);
        tempTable.combineMelds(0, 1);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O3, O4, O5, O6));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(R8, G8, B8, O8));

        //End turn
        handler.submit();
        handler.endTurn();

        //Checking the turn
        assertThat(table.getPlayingMelds().size(), is(2));
        assertThat(table.getPlayingMelds().get(0).tiles(), contains(O3, O4, O5, O6));
        assertThat(table.getPlayingMelds().get(1).tiles(), contains(R8, G8, B8, O8));
        assertThat(hand.getMelds().size(), is(0));
    }

    @Test
    //Forming new set
    public void scenario_2_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(O5, O6, O7);

        // Set up the table
        Table table = new Table();
        Meld setOfSet = Meld.createMeld(O8, R8, G8, B8);
        table.addMeld(setOfSet);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        /* Start the turn  */
        //Adding O8, R8, G8, B8 to tempt table
        handler.takeTableMeld(0);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O8, R8, G8, B8));

        hand.formMeld(0, 1, 2);
        assertThat(hand.getMelds().get(0).tiles(), contains(O5, O6, O7));

        //Adding {O5, O6, O7} to manipulation table
        handler.playFromHand(0);
        assertThat(tempTable.getMelds().get(1).tiles(), contains(O5, O6, O7));

        //taking O8 out of set
        tempTable.detach(0, 0);
        assertThat(tempTable.getMelds().get(2).tiles(), contains(O8));

        //Combine with {O5, O6, O7}
        tempTable.combineMelds(0, 2);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(R8, G8, B8));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(O5, O6, O7, O8));

        //End Turn
        handler.submit();
        handler.endTurn();

        //Checking the turn
        assertThat(table.getPlayingMelds().size(), is(2));
        assertThat(table.getPlayingMelds().get(0).tiles(), contains(R8, G8, B8));
        assertThat(table.getPlayingMelds().get(1).tiles(), contains(O5, O6, O7, O8));
        assertThat(hand.getMelds().size(), is(0));
    }

    @Test
    //Add and split
    public void scenario_3_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(O5, B8, R8);

        // Set up the table
        Table table = new Table();
        Meld setOfRun = Meld.createMeld(O6, O7, O8);
        table.addMeld(setOfRun);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        /* Start the turn  */
        //Adding O6, O7, O8 to tempt table
        handler.takeTableMeld(0);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O6, O7, O8));

        //Forming tiles to play from hand {O5} and {B8, R8}
        hand.formMeld(0);
        hand.formMeld(0, 1);
        assertThat(hand.getMelds().get(0).tiles(), contains(O5));
        assertThat(hand.getMelds().get(1).tiles(), contains(B8, R8));

        //Adding {O5} and to {B8, R8} manipulation table
        handler.playFromHand(0);
        handler.playFromHand(0);
        assertThat(tempTable.getMelds().get(1).tiles(), contains(O5));
        assertThat(tempTable.getMelds().get(2).tiles(), contains(B8, R8));

        //Splitting {O8} from {O6, O7, O8}
        tempTable.split(0, 2);
        assertThat(tempTable.getMelds().size(), is(4));
        assertThat(tempTable.getMelds().get(2).tiles(), contains(O6, O7));
        assertThat(tempTable.getMelds().get(3).tiles(), contains(O8));

        //Combining to form {O5, O6, O7} and {B8, R8, O8}
        tempTable.combineMelds(0, 2);
        tempTable.combineMelds(0, 1);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O5, O6, O7));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(B8, R8, O8));

        //End turn
        handler.submit();
        handler.endTurn();

        //Checking the turn
        assertThat(table.getPlayingMelds().size(), is(2));
        assertThat(table.getPlayingMelds().get(0).tiles(), contains(O5, O6, O7));
        assertThat(table.getPlayingMelds().get(1).tiles(), contains(B8, R8, O8));
        assertThat(hand.getMelds().size(), is(0));
    }

    @Test
    //Combined Split
    public void scenario_4_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(B8);

        // Set up the table
        Table table = new Table();
        Meld setOfSet = Meld.createMeld(O8, R8, B8, G8);
        Meld setOfRun = Meld.createMeld(G5, G6, G7, G8);
        table.addMeld(setOfSet);
        table.addMeld(setOfRun);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        /* Start the turn  */
        //Adding  {O8, R8, B8, G8} and {G5, G6, G7, G8} to tempt table
        handler.takeTableMeld(0);
        handler.takeTableMeld(0);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(O8, R8, B8, G8));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(G5, G6, G7, G8));

        //Forming tiles to play from hand {R8}
        hand.formMeld(0);
        assertThat(hand.getMelds().get(0).tiles(), contains(B8));

        //Adding {R8} manipulation table
        handler.playFromHand(0);
        assertThat(tempTable.getMelds().get(2).tiles(), contains(B8));

        //Splitting {O8} from  {O8, R8, B8, G8} and {G8} from {G5, G6, G7, G8}
        tempTable.detach(0, 0);
        tempTable.split(0, 3);
        assertThat(tempTable.getMelds().get(2).tiles(), contains(O8));
        assertThat(tempTable.getMelds().get(4).tiles(), contains(G8));

        //Combining {B8}, {O8}, {G8} to form {B8, O8, G8}
        tempTable.combineMelds(0, 2, 4);
        assertThat(tempTable.getMelds().get(2).tiles(), contains(B8, O8, G8));

        //End turn
        handler.submit();
        handler.endTurn();

        //Checking the turn
        assertThat(table.getPlayingMelds().size(), is(3));
        assertThat(table.getPlayingMelds().get(0).tiles(), contains(R8, B8, G8));
        assertThat(table.getPlayingMelds().get(1).tiles(), contains(G5, G6, G7));
        assertThat(table.getPlayingMelds().get(2).tiles(), contains(B8, O8, G8));
        assertThat(hand.getMelds().size(), is(0));
    }

    @Test
    //Multiple Splits
    public void scenario_5_Test() throws IllegalAccessException {
        //Set up controllers and hand
        player.setStatus(PlayerStatus.ICE_BROKEN);
        Hand hand = player.hand();
        hand.addTiles(O3, R8);

        // Set up the table
        Table table = new Table();
        Meld setOfRun1 = Meld.createMeld(B6, B7, B8);
        Meld setOfRun2 = Meld.createMeld(G6, G7, G8);
        Meld setOfSet = Meld.createMeld(O4, O5, O6, O7, O8);
        table.addMeld(setOfRun1);
        table.addMeld(setOfRun2);
        table.addMeld(setOfSet);

        //Create action handler
        ActionHandler handler = new ActionHandler(player, table);
        ManipulationTable tempTable = handler.getManipulationTable();

        //Test table before turn
        assertThat(tempTable.getMelds().isEmpty(), is(true));

        //Taking meld {B6, B7, B8}, {G6, G7, G8} and {O4, O5, O6, O7, O8} into temp table
        handler.takeTableMeld(0);
        handler.takeTableMeld(0);
        handler.takeTableMeld(0);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(B6, B7, B8));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(G6, G7, G8));
        assertThat(tempTable.getMelds().get(2).tiles(), contains(O4, O5, O6, O7, O8));

        //Forming {O3} and {R8} from hand to play
        hand.formMeld(0);
        hand.formMeld(0);
        assertThat(hand.getMelds().get(0).tiles(), contains(O3));
        assertThat(hand.getMelds().get(1).tiles(), contains(R8));

        //Adding {O3} and {R8} to temp table
        handler.playFromHand(0);
        handler.playFromHand(0);
        assertThat(tempTable.getMelds().get(3).tiles(), contains(O3));
        assertThat(tempTable.getMelds().get(4).tiles(), contains(R8));

        //Splitting {B8} in {B6, B7, B8} , {G8} in {G6, G7, G8} and {O8} in {O4, O5, O6, O7, O8}
        tempTable.split(0, 2);
        assertThat(tempTable.getMelds().get(5).tiles(), contains(B8));
        tempTable.split(0, 2);
        assertThat(tempTable.getMelds().get(6).tiles(), contains(G8));
        tempTable.split(0, 4);
        assertThat(tempTable.getMelds().get(7).tiles(), contains(O8));
        tempTable.combineMelds(1, 3, 5, 7);
        assertThat(tempTable.getMelds().get(4).tiles(), contains(R8, B8, G8, O8));

        //========================================================================
        tempTable.split(1, 1);
        tempTable.split(1, 1);
        tempTable.split(1, 3);
        tempTable.split(6, 2);
        tempTable.combineMelds(2, 4, 8);
        tempTable.combineMelds(2, 3, 4);
        tempTable.combineMelds(0, 2);
        assertThat(tempTable.getMelds().get(0).tiles(), contains(R8, B8, G8, O8));
        assertThat(tempTable.getMelds().get(1).tiles(), contains(B6, G6, O6));
        assertThat(tempTable.getMelds().get(2).tiles(), contains(B7, G7, O7));
        assertThat(tempTable.getMelds().get(3).tiles(), contains(O3, O4, O5));

        //End turn
        handler.submit();
        handler.endTurn();

        //Checking the turn
        assertThat(table.getPlayingMelds().size(), is(4));
        assertThat(table.getPlayingMelds().get(0).tiles(), contains(R8, B8, G8, O8));
        assertThat(table.getPlayingMelds().get(1).tiles(), contains(B6, G6, O6));
        assertThat(table.getPlayingMelds().get(2).tiles(), contains(B7, G7, O7));
        assertThat(table.getPlayingMelds().get(3).tiles(), contains(O3, O4, O5));
         assertThat(hand.getMelds().size(), is(0));
    }
}

