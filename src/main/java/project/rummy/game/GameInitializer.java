package project.rummy.game;

import project.rummy.entities.Table;
import project.rummy.entities.Player;

/**
 * This class is responsible to initialize a new game. Why we need this Factory class? We may see
 * want to init the game from file or with a predefined state so that we can test the whole game
 * easier.
 */
public interface GameInitializer {
    /**
     * Initialize a list of players
     */
    void initPlayers(Game game);

    /**
     * Initialize the table for the game
     */
    void initTable(Game game);

    /**
     * Initialize an empty table for testing
     *
     * @param game
     */
    void initEmptyTable(Game game);

    /**
     * Initialize game state
     */
    void initializeGameState(Player[] players, Table table);
}
