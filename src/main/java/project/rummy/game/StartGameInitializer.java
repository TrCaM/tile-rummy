package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.Strategy1;

import java.util.stream.Stream;

public class StartGameInitializer implements GameInitializer {

    @Override
    public void initPlayers(Game game) {
        Controller[] controllers = new Controller[]{
                new ManualController(),
                new AutoController(game, new Strategy1()),
                new AutoController(game, new Strategy1()),
                new AutoController(game, new Strategy1())};
        Player[] players = new Player[4];
        players[0] = new Player("The HUMAN", controllers[0], 0);
        for (int i=1; i<4; i++) {
            players[i] = new Player(String.format("Player %d", i), controllers[i], i);
        }
        game.setUpPlayer(players);
    }

    @Override
    public void initTable(Game game) {
        Table table = new Table();
        table.initTiles();
        table.shuffle();
        game.setUpTable(table);
    }

    @Override
    public void  initEmptyTable(Game game){
        Table table = new Table();
        game.setUpTable(table);
    }

    /**
     * Generate a normal game (drawAndEndTurn 14 cards to each player)
     */
    @Override
    public void initializeGameState(Player[] players, Table table) {
        // TODO: Generate the initial game state for players (drawAndEndTurn 14 tiles for each), making sure that
        // the table is empty
        //
        Stream.of(players).forEach(player -> {
            for (int i=0; i<1; i++) {
                player.hand().addTile(table.drawTile());
            }
            player.hand().sort();
        });
    }
}
