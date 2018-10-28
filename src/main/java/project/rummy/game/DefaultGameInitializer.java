package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Hand;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.AggressiveStrategy;
import project.rummy.strategies.ModerateStrategy;
import project.rummy.strategies.PassiveStrategy;

import java.util.stream.Stream;

public class DefaultGameInitializer implements GameInitializer {

  @Override
  public Player[] initPlayers() {
    Controller[] controllers = new Controller[]{
        new ManualController(),
        new AutoController(new AggressiveStrategy()),
        new AutoController(new PassiveStrategy()),
        new AutoController(new ModerateStrategy())};
    Player[] players = new Player[4];
    for (int i=0; i<4;i++) {
      players[i] = new Player(controllers[i]);
    }
    return players;
  }

  @Override
  public Table initTable() {
    Table table = new Table();
    table.initTiles();
    table.shuffle();
    return table;
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
