package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;

public class DefaultGameInitializer implements GameInitializer {

  @Override
  public void initPlayers(Game game) {
    Controller[] controllers = new Controller[]{
        new ManualController(),
        new AutoController(new Strategy1(game)),
        new AutoController(new Strategy2(game)),
        new AutoController(new Strategy3(game))};
    Player[] players = new Player[4];
    players[0] = new Player("You", controllers[0]);
    for (int i=1; i<4; i++) {
      players[i] = new Player(String.format("Player %d", i), controllers[i]);
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

  /**
   * Generate a normal game (draw 14 cards to each player)
   */
  @Override
  public void initializeGameState(Player[] players, Table table) {
    // TODO: Generate the initial game state for players (draw 14 tiles for each), making sure that
    // the table is empty
  }

}
