package project.rummy.game;

import project.rummy.entities.Player;
import project.rummy.entities.Table;

public class GameStore {
  private GameInitializer initializer;

  public GameStore(GameInitializer initializer) {
    this.initializer = initializer;
  }

  public Game initializeGame() {
    Player[] players = initializer.initPlayers();
    Table table = initializer.initTable();
    GameState status = initializer.initGameStatus();
    return new Game(players, table, status);
  }
}
