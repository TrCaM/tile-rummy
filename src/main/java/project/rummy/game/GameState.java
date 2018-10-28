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
  private List<Meld> tableMelds;
  private HandData[] handsData;

  static GameState generateState(Game game) {
    GameState gameState = new GameState();
    gameState.turnNumber = game.getTurnNumber();
    gameState.freeTilesCount = game.getTable().getFreeTiles().size();
    gameState.tableMelds = game.getTable().getPlayingMelds();
    gameState.handsData = Stream.of(game.getPlayers())
        //TODO: create HandData class and method toHandData in Hand class
        .map(Player::hand).map(Hand::toHandData).toArray(HandData[]::new);
    return gameState;
  }

  public int getFreeTilesCount() {
    return freeTilesCount;
  }

  public List<Meld> getTableMelds() {
    return tableMelds;
  }

  public HandData[] getHandsData() {
    return handsData;
  }

  public int getTurnNumber() {
    return turnNumber;
  }
}
