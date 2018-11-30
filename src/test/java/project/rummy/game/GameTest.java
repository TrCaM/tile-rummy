package project.rummy.game;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.*;
import sun.font.TrueTypeFont;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

public class GameTest {


    private GameInitializer initializer;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private Controller controller;

    @Before
    public void setUp() {
        initializer = new DefaultGameInitializer();
    }

    private final Tile O3 = Tile.createTile(Color.ORANGE, 3);
    private final Tile O4 = Tile.createTile(Color.ORANGE, 5);
    private final Tile O5 = Tile.createTile(Color.ORANGE, 6);
    private final Tile B9 = Tile.createTile(Color.BLACK, 9);
    private final Tile B12 = Tile.createTile(Color.BLACK, 12);
    private final Tile B5 = Tile.createTile(Color.BLACK, 5);
    private final Tile G3 = Tile.createTile(Color.GREEN, 3);
    private final Tile G5 = Tile.createTile(Color.GREEN, 5);
    private final Tile R8 = Tile.createTile(Color.RED, 8);
    private final Tile R7 = Tile.createTile(Color.RED, 7);

    @Test
    public void playerWinWithNoTiles() {
        Game game = new Game(initializer, false);
        initializer.initPlayers(game);
        initializer.initTable(game);
        game.startGame();

        game.getPlayers()[0].hand().addTiles();
        game.getPlayers()[1].hand().addTiles(B5, B12);
        game.getPlayers()[2].hand().addTiles(O3, R8, B9);
        game.getPlayers()[3].hand().addTiles(B5, G3, G5, O4, O5);

        assertThat(game.getPlayers()[0].hand().getTiles().isEmpty(), is(true));
        assertThat(game.getPlayers()[1].hand().getTiles(), contains(B5, B12));
        assertThat(game.getPlayers()[2].hand().getTiles(), contains(O3, R8, B9));
        assertThat(game.getPlayers()[3].hand().getTiles(), contains(B5, G3, G5, O4, O5));
        assertThat(game.getWinner(), is(0));
    }

    @Test
    public void playerWinWithLeastPoint() {
        Game game = new Game(initializer, false);
        initializer.initPlayers(game);
        initializer.initEmptyTable(game);
        game.nextTurn();


        game.getPlayers()[0].hand().addTiles(R7);
        game.getPlayers()[1].hand().addTiles(B5, B12);
        game.getPlayers()[2].hand().addTiles(O3, R8, B9);
        game.getPlayers()[3].hand().addTiles(B5, G3, G5, O4, O5);

        assertThat(game.getTable().getFreeTiles().isEmpty(), is(true) );
        assertThat(game.getWinner(), is(0) );
    }
}
