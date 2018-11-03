package project.rummy.behaviors;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;
import project.rummy.game.LoadGameInitializer;

import static org.junit.Assert.*;

import java.util.*;


class LoadGameInitializerTest {
    //Orange Tiles
    private final Tile O1 = Tile.createTile(Color.ORANGE, 1);
    private final Tile O2 = Tile.createTile(Color.ORANGE, 2);
    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);
    private final Tile O4 = Tile.createTile(Color.ORANGE, 4);
    private final Tile O5 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O6 = Tile.createTile(Color.ORANGE, 6);
    private final Tile O7 = Tile.createTile(Color.ORANGE, 7);
    private final Tile O8 = Tile.createTile(Color.ORANGE, 8);
    private final Tile O9 = Tile.createTile(Color.ORANGE, 9);
    private final Tile O10 = Tile.createTile(Color.ORANGE, 10);

    //Green Tiles
    private final Tile G1 = Tile.createTile(Color.GREEN, 1);
    private final Tile G2 = Tile.createTile(Color.GREEN, 2);
    private final Tile G3 = Tile.createTile(Color.GREEN, 3);
    private final Tile G4 = Tile.createTile(Color.GREEN, 4);
    private final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private final Tile G6 = Tile.createTile(Color.GREEN, 6);
    private final Tile G7 = Tile.createTile(Color.GREEN, 7);
    private final Tile G8 = Tile.createTile(Color.GREEN, 8);
    private final Tile G9 = Tile.createTile(Color.GREEN, 9);
    private final Tile G10 = Tile.createTile(Color.GREEN, 10);

    //Black Tiles
    private final Tile B1 = Tile.createTile(Color.BLACK, 1);
    private final Tile B2 = Tile.createTile(Color.BLACK, 2);
    private final Tile B3 = Tile.createTile(Color.BLACK, 3);
    private final Tile B4 = Tile.createTile(Color.BLACK, 4);
    private final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private final Tile B6 = Tile.createTile(Color.BLACK, 6);
    private final Tile B7 = Tile.createTile(Color.BLACK, 7);
    private final Tile B8 = Tile.createTile(Color.BLACK, 8);
    private final Tile B9 = Tile.createTile(Color.BLACK, 9);
    private final Tile B10 = Tile.createTile(Color.BLACK, 10);

    //Red Tiles
    private final Tile R1 = Tile.createTile(Color.RED, 1);
    private final Tile R2 = Tile.createTile(Color.RED, 2);
    private final Tile R3 = Tile.createTile(Color.RED, 3);
    private final Tile R4 = Tile.createTile(Color.RED, 4);
    private final Tile R5 = Tile.createTile(Color.RED, 5);
    private final Tile R6 = Tile.createTile(Color.RED, 6);
    private final Tile R7 = Tile.createTile(Color.RED, 7);
    private final Tile R8 = Tile.createTile(Color.RED, 8);
    private final Tile R9 = Tile.createTile(Color.RED, 9);
    private final Tile R10 = Tile.createTile(Color.RED, 10);


    private int turnNumber;
    private int freeTilesCount;
    private TableData tableData;
    private HandData[] handsData;
    private PlayerData[] playerData;
    private PlayerStatus[] statuses;
    private int currentPlayer;

    private GameState state;

    @Before
    public void setup() {

    }

    @Test
    public void loadGameInitializer_test(){
        state = new GameState();
        List<List<Tile>> tileList = new ArrayList<>();
        List<Tile> freeTiles = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<Meld> tableMelds = new ArrayList<>();

        // Creating  list of tiles, free tiles and players
        List<Tile> list1 = new ArrayList<>();
        list1.addAll(Arrays.asList(B10, R5, O1, B8, B9, O7, G1));
        tileList.add(list1);

        List<Tile> list2 = new ArrayList<>();
        list2.addAll(Arrays.asList(B3, G7, B7, R7, G3, O10));
        tileList.add(list2);

        List<Tile> list3 = new ArrayList<>();
        list3.addAll(Arrays.asList(G1, G2, R1, R9, G8, O9));
        tileList.add(list3);

        List<Tile> list4 = new ArrayList<>();
        list4.addAll(Arrays.asList(O8, G5, G9, B1, B2, B5, R2, R3));
        tileList.add(list4);

        handsData = new HandData[4];
        playerData = new PlayerData[4];

        statuses = new PlayerStatus[4];
        for (int i = 0; i < 4; i++) {
            statuses[i] = PlayerStatus.START;
        }

        for (int i = 0; i < 4; i++) {
            Hand hand = new Hand(tileList.get(i));
            handsData[i] = new HandData(hand);
            playerData[i] = new PlayerData("player" + i, "human");
        }

        //set up melds on the table
        tableMelds.add(Meld.createMeld(O4, B4, G4, R4));
        tableMelds.add(Meld.createMeld(B6, O6, G6, R6));
        tableMelds.add(Meld.createMeld(O10, B10, G10, R10));
        tableMelds.add(Meld.createMeld(O1, O2, O3, O4, O5, O6));
        tableMelds.add(Meld.createMeld(B8, B9, B10));
        tableMelds.add(Meld.createMeld(R5, R6, R7, R8, R9, R10));


        //Setting up deck
        freeTiles.addAll(Arrays.asList(R5, R6, R7, R8, R9, R10));

        //Setting up table
        Table table = new Table(freeTiles);
        for (Meld m : tableMelds) {
            table.addMeld(m);
        }


        //setup data
        turnNumber = 11;
        freeTilesCount = freeTiles.size();
        tableData = new TableData(table);
        currentPlayer = 0;

        state.setTurnNumber(turnNumber);
        state.setFreeTilesCount(freeTiles.size());
        state.setTableData(table.toTableData());
        state.setHandsData(handsData);
        state.setPlayerData(playerData);
        state.setStatuses(statuses);
        state.setCurrentPlayer(currentPlayer);

        GameStore gameStore = new GameStore(new LoadGameInitializer(state));
        Game game = gameStore.initializeGame();

        GameState stateTest = game.generateGameState();

        assertTrue(stateTest.getPlayerStatuses()[0] == PlayerStatus.START);
        assertTrue(stateTest.getPlayerData()[0].controllerType.equals("human"));
        assertTrue(stateTest.getTableData().freeTiles.contains(R5));
        assertTrue(stateTest.getTableData().freeTiles.contains(R8));
        assertTrue(stateTest.getTableData().freeTiles.contains(R10));
        assertFalse(stateTest.getTableData().freeTiles.contains(B5));

        for(Meld meld: tableMelds) {
            assertTrue(stateTest.getTableData().melds.contains(meld));
        }
        assertEquals(stateTest.getTurnNumber(), turnNumber);

        assertTrue(stateTest.getHandsData()[2].melds.isEmpty());

        for(Tile t: list3){
            assertTrue(stateTest.getHandsData()[2].tiles.contains(t));
        }

        assertTrue(stateTest.getPlayerData()[1].name.equals(playerData[1].name));
    }
}