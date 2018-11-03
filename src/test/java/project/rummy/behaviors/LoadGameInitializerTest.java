package project.rummy.behaviors;

import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;
import project.rummy.game.LoadGameInitializer;
import project.rummy.strategies.Strategy;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;

import static org.junit.Assert.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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

    @Mock
    private Controller controller;

    private  GameState  state;
    private  Game game;

    @Before
    public void setUp() {
        // Creating  list of tiles, free tiles and players
        List<Tile> list1 = new ArrayList<>();
        List<Tile> list2 = new ArrayList<>();
        List<Tile> list3 = new ArrayList<>();
        List<Tile> list4 = new ArrayList<>();
        List<Tile> Freetiles = new ArrayList<>();
        List<Player> listOfPlayers = new ArrayList<>();

        //Adding list of tiles into hand
        list1.addAll(Arrays.asList(B10, R5, O1, B8, B9, O7, G1));
        Hand hand1 = new Hand(list1);
        list2.addAll(Arrays.asList(B3, G7, B7, R7, G3, O10));
        Hand hand2 = new Hand(list2);
        list3.addAll(Arrays.asList(G1, G2, R1, R9, G8, O9));
        Hand hand3 = new Hand(list3);
        list4.addAll(Arrays.asList(O8, G5, G9, B1, B2, B5, R2, R3));
        Hand hand4 = new Hand(list4);

        //Creating controller for each player
//        Controller control1 = new AutoController(new Strategy1(game));
//        Controller control2 = new AutoController(new Strategy2(game));
//        Controller control3 = new AutoController(new Strategy3(game));
//        Controller control4 = new AutoController(new Strategy1(game));

        //Creating players
        Player p1 = new Player("Player1", controller, hand1, PlayerStatus.START);
        Player p2 = new Player("Player2", controller, hand2, PlayerStatus.START);
        Player p3 = new Player("Player3", controller, hand3, PlayerStatus.START);
        Player p4 = new Player("Player4", controller, hand4, PlayerStatus.START);

        //Creating a list of players
        listOfPlayers.addAll(Arrays.asList(p1,p2,p3,p4));


        Meld set1 = Meld.createMeld(O4, B4, G4, R4);
        Meld set2 = Meld.createMeld(B6, O6, G6, R6);
        Meld set3 = Meld.createMeld(O10, B10, G10, R10);
        Meld run1 = Meld.createMeld(O1, O2, O3, O4, O5, O6);
        Meld run2 = Meld.createMeld(B8, B9, B10);
        Meld run3 = Meld.createMeld(R5, R6, R7, R8, R9, R10);

        //Setting up deck
        Freetiles.addAll(Arrays.asList(R5, R6, R7, R8, R9, R10));

        //Setting up table
        Table table = new Table(Freetiles);
        table.addMeld(set1);
        table.addMeld(set2);
        table.addMeld(set3);
        table.addMeld(run1);
        table.addMeld(run2);
        table.addMeld(run3);

        //Setting up game
        game.setUpPlayer(listOfPlayers.toArray(new Player[4]));
        game.setUpTable(table);

        //Generating a game state from game
        state = GameState.generateState(game);
    }

    @Test
    public void LoadGameInitiallizer_test(){
        GameStore gameStore = new GameStore(new LoadGameInitializer(state));
        GameState gameTest = GameState.generateState(gameStore.initializeGame());


    }


}