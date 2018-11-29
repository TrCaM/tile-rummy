package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.Strategy1;

import java.util.stream.Stream;

public class StartGameInitializer extends DefaultGameInitializer {
    /**
     * Generate a normal game (drawAndEndTurn 14 cards to each player)
     */
    @Override
    public void initializeGameState(Player[] players, Table table) {
        Stream.of(players).forEach(player -> {
            for (int i=0; i<1; i++) {
                player.hand().addTile(table.drawTile());
            }
            player.hand().sort();
        });
    }
}
