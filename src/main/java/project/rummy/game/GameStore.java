package project.rummy.game;

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

  public void setInitializer(GameInitializer initializer) {
    this.initializer = initializer;
  }
}
