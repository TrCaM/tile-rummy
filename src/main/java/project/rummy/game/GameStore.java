package project.rummy.game;

import project.rummy.entities.Player;
import project.rummy.entities.Table;

public class GameStore {
  private GameInitializer initializer;
  private StartGameInitializer startGameInitializer;

  public GameStore(GameInitializer initializer, StartGameInitializer startGameInitializer) {
    this.initializer = initializer;
    this.startGameInitializer = startGameInitializer;
  }

  public Game initializeGame() {
    Game game = new Game();
    initializer.initPlayers(game);
    initializer.initTable(game);
    initializer.initializeGameState(game.getPlayers(), game.getTable());
    return game;
  }

  public Game initializeGameStart() {
    Game game = new Game();
    startGameInitializer.initPlayers(game);
    startGameInitializer.initTable(game);
    startGameInitializer.initializeGameState(game.getPlayers(), game.getTable());
    return game;
  }

}
