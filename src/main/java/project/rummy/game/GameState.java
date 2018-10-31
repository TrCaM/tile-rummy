package project.rummy.game;

import project.rummy.entities.*;

import java.util.List;
import java.util.stream.Stream;

/**
 * Generate a general data object for displaying the game, or for the strategies to analyze and find
 * the best possible moves.
 */
public class GameState {
  private int turnNumber;
  private int freeTilesCount;
  private TableData tableData;
  private HandData[] handsData;
  private PlayerStatus[] statuses;
  private int currentPlayer;

  static GameState generateState(Game game) {
    GameState gameState = new GameState();
    gameState.turnNumber = game.getTurnNumber();
    gameState.currentPlayer = game.getCurrentPlayer();
    gameState.freeTilesCount = game.getTable().getFreeTiles().size();
    gameState.tableData = game.getTable().toTableData();
    gameState.handsData = Stream.of(game.getPlayers())
        .map(Player::hand).map(Hand::toHandData).toArray(HandData[]::new);
    gameState.statuses = Stream.of(game.getPlayers())
        .map(Player::status).toArray(PlayerStatus[]::new);
    return gameState;
  }

  public int getFreeTilesCount() {
    return freeTilesCount;
  }

  public TableData getTableData() {
    return tableData;
  }

  public HandData[] getHandsData() {
    return handsData;
  }

  public int getTurnNumber() {
    return turnNumber;
  }

  public int getCurrentPlayer() {
    return this.currentPlayer;
  }

  public PlayerStatus[] getPlayerStatuses() {
    return this.statuses;
  }
}
