package project.rummy.game;

public class GameFactory {
  private GameInitializer initializer;

  public GameFactory(GameInitializer initializer) {
    this.initializer = initializer;
  }

  public Game initializeGame() {
    Game game = new Game(initializer, initializer instanceof NetworkGameInitializer);
    initializer.initPlayers(game);
    initializer.initTable(game);
    initializer.initializeGameState(game.getPlayers(), game.getTable());
    return game;
  }
}
