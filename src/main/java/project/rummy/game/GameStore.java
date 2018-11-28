package project.rummy.game;

import project.rummy.entities.Player;
import project.rummy.entities.Table;

public class GameStore {
  private GameInitializer initializer;

  public GameStore(GameInitializer initializer) {
    this.initializer = initializer;
  }


  public Game initializeGame() {
    Game game = new Game(initializer instanceof NetworkGameInitializer);
    initializer.initPlayers(game);
    initializer.initTable(game);
    initializer.initializeGameState(game.getPlayers(), game.getTable());
    return game;
  }

  public Game initializeGameStart() {
    Game game = new Game();
    initializer.initPlayers(game);
    initializer.initTable(game);
    initializer.initializeGameState(game.getPlayers(), game.getTable());
    return game;
  }

}
