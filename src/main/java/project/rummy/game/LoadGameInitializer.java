package project.rummy.game;

import project.rummy.entities.Player;
import project.rummy.entities.Table;

/**
 * Initialize game from the predefined game state. Useful for testing
 * TODO: Write tests and implement all the methods
 */
public class LoadGameInitializer implements GameInitializer{
  private GameState state;

  public LoadGameInitializer(GameState state) {
    this.state = state;
  }

  @Override
  public void initPlayers(Game game) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void initTable(Game game) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void initializeGameState(Player[] players, Table table) {
    /* TODO: We can actually left this empty if you already do everything else before */
    throw new UnsupportedOperationException();
  }
}
