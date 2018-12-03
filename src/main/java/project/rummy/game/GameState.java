package project.rummy.game;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.entities.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generate a general data object for displaying the game, or for the strategies to analyze and find
 * the best possible moves.
 */
public class GameState extends Component implements Serializable {
  private int turnNumber;
  private int freeTilesCount;
  private TableData tableData;
  private HandData[] handsData;
  private PlayerData[] playerData;
  private PlayerStatus[] statuses;
  private TurnStatus turnStatus;
  private int currentPlayer;
  private GameStatus status;
  private int nextMeldId;
  private int playerCount;
  private int controlledPlayer;

  private boolean isTurnBeginning;
  private List<List<Tile>> findFirstTileList;

  public boolean isTurnBeginning() {
    return isTurnBeginning;
  }



  public static GameState generateState(Game game) {
    GameState gameState = new GameState();
    gameState.turnNumber = game.getTurnNumber();
    gameState.currentPlayer = game.getCurrentPlayer();
    gameState.freeTilesCount = game.getTable().getFreeTiles().size();
    gameState.tableData = game.getTable().toTableData();
    gameState.handsData = Stream.of(game.getPlayers())
        .map(Player::hand).map(Hand::toHandData).toArray(HandData[]::new);
    gameState.statuses = Stream.of(game.getPlayers())
        .map(Player::status).toArray(PlayerStatus[]::new);
    gameState.playerData = Stream.of(game.getPlayers())
        .map(Player::toPlayerData).toArray(PlayerData[]::new);
    gameState.playerCount = game.getPlayersCount();
    gameState.turnStatus = game.getTurnStatus();
    gameState.status = game.getStatus();
    gameState.nextMeldId = Meld.getNextId() + 100;
    gameState.isTurnBeginning = game.isTurnBeginning();
    gameState.findFirstTileList = game.getFindFirstTileList();
    gameState.controlledPlayer = game.getControlledPlayerIndex();
    return gameState;
  }

  public int getControlledPlayer() {
    return controlledPlayer;
  }

  public PlayerData[] getPlayerData() {
    return playerData;
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

  public List<List<Tile>> getFindFirstTileList() {
    return findFirstTileList;
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

  public TurnStatus getTurnStatus() {
    return turnStatus;
  }

  public GameStatus getGameStatus() {
    return status;
  }

  int getPlayerCount() {
    return playerCount;
  }

  public void setTurnNumber(int turnNumber) {
    this.turnNumber = turnNumber;
  }

  public void setFreeTilesCount(int freeTilesCount) {
    this.freeTilesCount = freeTilesCount;
  }

  public void setTableData(TableData tableData) {
    this.tableData = tableData;
  }

  public void setPlayerData(PlayerData[] playerData) {
    this.playerData = playerData;
  }

  public void setHandsData(HandData[] handsData) {
    this.handsData = handsData;
  }

  public void setStatuses(PlayerStatus[] statuses) {
    this.statuses = statuses;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public void setTurnStatus(TurnStatus turnStatus) {
    this.turnStatus = turnStatus;
  }

  public void setGameStatus(GameStatus status) {
    this.status = status;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }


  public int getNextMeldId() {
    return nextMeldId;
  }
}
