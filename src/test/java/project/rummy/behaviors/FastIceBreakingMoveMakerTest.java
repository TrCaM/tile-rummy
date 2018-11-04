package project.rummy.behaviors;

import org.junit.jupiter.api.Test;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;
import project.rummy.game.LoadGameInitializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FastIceBreakingMoveMakerTest {


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
    private final Tile O9_2 = Tile.createTile(Color.ORANGE, 9);
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
    private final Tile B13 = Tile.createTile(Color.BLACK, 13);

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
    private final Tile O13 = Tile.createTile(Color.RED, 13);



    private int turnNumber;
    private int freeTilesCount;
    private TableData tableData;
    private HandData[] handsData;
    private PlayerData[] playerData;
    private PlayerStatus[] statuses;
    private int currentPlayer;

    private GameState state;


    @Test
    public void FastIceBreakingMoveMaker_test() {
        CommandProcessor processor = CommandProcessor.getInstance();
        state = new GameState();

        List<List<Tile>> tileList = new ArrayList<>();
        List<Tile> freeTiles = new ArrayList<>();
        List<Meld> tableMelds = new ArrayList<>();

        // Creating  list of tiles, free tiles and players
        List<Tile> list1 = new ArrayList<>();
        list1.addAll(Arrays.asList(O10, B10,  R1, B1, R4, G10, G1));
        tileList.add(list1);

        List<Tile> list2 = new ArrayList<>();
        list2.addAll(Arrays.asList(B3, G7, B7, R7, G3, O10, O1));
        tileList.add(list2);

        List<Tile> list3 = new ArrayList<>();
        list3.addAll(Arrays.asList(G1, G2, R1, R9, G8, O9, O5));
        tileList.add(list3);

        List<Tile> list4 = new ArrayList<>();
        list4.addAll(Arrays.asList(R2, R9, B7, B9, B10, B13, G2, O1, O2, O5, O7, O9_2, O9, O13));
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
            playerData[i] = new PlayerData("player" + i, "strategy1");
        }


        //Setting up deck
        freeTiles.addAll(Arrays.asList(R5, R6, R7, R8, R9, R10));

        //Setting up table
        Table table = new Table(freeTiles);
        for (Meld m : tableMelds) {
            table.addMeld(m);
        }


        //setup data
        turnNumber = 12;
        freeTilesCount = freeTiles.size();
        tableData = new TableData(table);
        currentPlayer = 3;

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

        processor.setUpGame(game);

        //O10, B10,  R1, B1, R4, G10, G1
        game.nextTurn();
        processor.proccessAllCommands();
        GameState newState = game.generateGameState();
        assertEquals(1,newState.getHandsData()[newState.getCurrentPlayer()].tiles.size());

        //B3, G7, B7, R7, G3, O10, O1
        game.nextTurn();
        processor.proccessAllCommands();
        GameState newState2 = game.generateGameState();
        assertEquals(8,newState2.getHandsData()[newState2.getCurrentPlayer()].tiles.size());

        //G1, G2, R1, R9, G8, O9. O5
        game.nextTurn();
        processor.proccessAllCommands();
        GameState newState3 = game.generateGameState();
        assertEquals(8,newState3.getHandsData()[newState3.getCurrentPlayer()].tiles.size());

        //O8, G5, G9, B1, B2, B3, R2, R3, O7, O6, O5
        game.nextTurn();
        processor.proccessAllCommands();
        GameState newState4 = game.generateGameState();
        assertEquals(8 ,newState4.getHandsData()[newState4.getCurrentPlayer()].tiles.size());
    }

}