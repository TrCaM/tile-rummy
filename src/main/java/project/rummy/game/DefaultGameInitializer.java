package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;
import project.rummy.strategies.StrategyDrawOnly;

import java.util.stream.Stream;

public class DefaultGameInitializer implements GameInitializer {

  @Override
  public void initPlayers(Game game) {
    Controller[] controllers = new Controller[]{
        new ManualController(),
        new AutoController(new Strategy1(game)),
        new AutoController(new Strategy2(game)),
        new AutoController(new StrategyDrawOnly(game))};
    Player[] players = new Player[4];
    players[0] = new Player("HUMAN", controllers[0]);
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

  @Override
  public void  initEmptyTable(Game game){
    Table table = new Table();
    game.setUpTable(table);
  }

  /**
   * Generate a normal game (draw 14 cards to each player)
   */
  @Override
  public void initializeGameState(Player[] players, Table table) {
    // TODO: Generate the initial game state for players (draw 14 tiles for each), making sure that
    // the table is empty
    //
    Stream.of(players).forEach(player -> {
      for (int i=0; i<14; i++) {
        player.hand().addTile(table.drawTile());
      }
      player.hand().sort();
    });
  }

}
