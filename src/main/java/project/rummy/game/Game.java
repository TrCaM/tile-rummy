package project.rummy.game;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.*;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game extends Component implements Observable {
  private Player[] players = null;
  private Table table = null;
  private int currentPlayer = 0;
  private List<Observer> observers;
  private int turnNumber = 0;
  private CommandProcessor commandProcessor;
  private boolean isGameEnd = false;
  private String winnerName = null;
  private TurnStatus turnStatus = null;
  private boolean preventUpdate;
  private int controlledPlayer = 0;
  private GameStatus status;
  private boolean isNetworkGame;
  private boolean isTurnStart = false;
  private int playersCount = 0;
  private List<List<Tile>> findFirstTileList;
  private GameInitializer initializer;
  private ManipulationTable manipulationTable;
  private GameMode gameMode;
  private int submitter;

  private int initialScore;

  public Game(GameInitializer initializer, boolean isNetworkGame) {
    super();
    this.initializer = initializer;
    this.observers = new ArrayList<>();
    this.commandProcessor = CommandProcessor.getInstance();
    this.preventUpdate = false;
    this.status = GameStatus.NOT_STARTED;
    this.isNetworkGame = isNetworkGame;
    findFirstTileList = new ArrayList<>();
    this.manipulationTable = ManipulationTable.getInstance();
    registerObserver(manipulationTable);
  }

  /////////////////
  // SET UP GAME //
  /////////////////
  public void setUpPlayers() {
    this.initializer.initPlayers(this);
  }
  public void setUpPlayers(Player[] players) {
    this.players = players;
    this.turnNumber = 0;
    this.currentPlayer = 0;
    this.playersCount = players.length;
  }

  public void setUpTable(Table table) {
    this.table = table;
  }

  ////////////////////////
  // SETTERS AND GETTER //
  ////////////////////////
  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public void setControlledPlayer(int index) {
    this.controlledPlayer = index;
  }

  void setTurnNumber(int num) {
    this.turnNumber = num;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public void setCurrentPlayer(int player) {
    this.currentPlayer = player;
  }

  private void setTurnStatus(TurnStatus turnStatus) {
    this.turnStatus = turnStatus;
  }

  public Player getControlledPlayer() {
    return players[controlledPlayer];
  }

  public Player getCurrentPlayerObject() {
    return players[currentPlayer];
  }

  Table getTable() {
    return this.table;
  }

  public Player[] getPlayers() {
    return players;
  }

  int getCurrentPlayer() {
    return currentPlayer;
  }

  int getTurnNumber() {
    return turnNumber;
  }

  public boolean isNetworkGame() {
    return isNetworkGame;
  }

  public boolean isGameEnd() {
    return isGameEnd;
  }

  public String getWinnerName() {
    return this.winnerName;
  }

  public GameStatus getStatus() {
    return status;
  }

  ManipulationTable getManipulationTable() {
    return manipulationTable;
  }

  boolean isTurnBeginning() {
    return isTurnStart;
  }

  List<List<Tile>> getFindFirstTileList() {
    return Collections.unmodifiableList(findFirstTileList);
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  TurnStatus getTurnStatus() {
    return turnStatus;
  }

  ////////////////
  // GAME LOGIC //
  ////////////////
  public void findFirstPlayer() {
    this.status = GameStatus.FINDING_FIRST;
    initializer.initTable(this);
    findFirstTileList.clear();
    for (int i = 0; i < playersCount; i++) {
      findFirstTileList.add(new ArrayList<>());
    }
    int turn = 1;
    int max;
    boolean[] status = new boolean[playersCount];
    int outs = 0;
    while (outs < playersCount - 1) {
      max = 0;
      for (int i = 0; i < playersCount; i++) {
        if (!status[i]) {
          try {
            Tile tile = table.drawTile();
            findFirstTileList.get(i).add(tile);
            max = tile.value() > max ? tile.value() : max;
          } catch (Exception e) {
            System.out.println();
          }
        }
      }
      for (int i = 0; i < playersCount; i++) {
        if (!status[i] && findFirstTileList.get(i).get(turn - 1).value() < max) {
          status[i] = true;
          outs++;
        }
      }
      turn++;
    }

    for (int i = 0; i < playersCount; i++) {
      if (!status[i]) {
        currentPlayer = i;
      }
    }
    notifyObservers();
  }

  public void startGame(boolean reset) {
    if (reset) {
      initializer.initTable(this);
      initializer.initializeGameState(players, table);
    }
    startGame();
  }

  public void startGame() {
    commandProcessor.setUpHandlers(
        Stream.of(players).map(player -> new ActionHandler(player, table)).collect(Collectors.toList()));
    turnNumber = 1;
    this.status = GameStatus.RUNNING;
    this.gameMode = GameMode.TURN_BASED;
    playTurn();
  }

  public void startAsynchronousGame() {
    initializer.initTable(this);
    initializer.initializeGameState(players, table);
    initialScore = players[controlledPlayer].hand().getScore();
    commandProcessor.setUpHandlers(
        Stream.of(players).map(player -> new ActionHandler(player, table)).collect(Collectors.toList()));
    turnNumber = 0;
    currentPlayer = -1;
    submitter = -1;
    this.status = GameStatus.RUNNING;
    this.gameMode = GameMode.ALL_PLAY;
    isTurnStart = true;
    for (Player player : players) {
      player.getController().playTurn();
    }
    notifyObservers();
  }

  private void playTurn() {
    this.players[currentPlayer].getController().playTurn();
    isTurnStart = true;
    notifyObservers();
  }

  private void resetTileHighlight() {
    players[currentPlayer].resetForNewTurn();
    table.resetForNewTurn();
  }

  private void endTurn() {
    this.players[currentPlayer].getController().closeInput();
    //commandProcessor.reset();
  }

  private void tryEndTurn() {
    this.status = GameStatus.TURN_END;
    if (!isNetworkGame) {
      nextTurn();
    } else {
      notifyObservers();
    }
  }

  public void nextTurn() {
    turnNumber++;
    this.players[currentPlayer].getController().endTurn();
    commandProcessor.reset();
    resetTileHighlight();
    this.status = GameStatus.RUNNING;
    currentPlayer = (currentPlayer + 1) % playersCount;
    playTurn();
  }

  /**
   * Check the current state of the game to see that if the game has ended:
   * + When a player has win the game (no tile in hand)
   * + When there's no free tile to drawAndEndTurn.
   *
   * @return the player index who won the game. That player either is the only player who has no
   * tile or has the least points in hand when there's no tile to drawAndEndTurn and play.
   * Return -1 if the game is not ended.
   */
  int getWinner() {
    for (int currentPlayer = 0; currentPlayer < playersCount; currentPlayer++) {
      if (players[currentPlayer].hand().size() == 0) {
        return currentPlayer;
      } else if (players[currentPlayer].hand().size() != 0 && table.getFreeTiles().isEmpty()) {

        int leastPoint = players[0].hand().getScore();
        int winner = 0;

        for (int i = 0; i < players.length; i++) {
          if (players[i].hand().getScore() < leastPoint) {
            leastPoint = players[i].hand().getScore();
            winner = i;
          }
        }
        return winner;
      }
    }
    return  -1;
  }

  public void endGame() {
    this.status = GameStatus.GAME_END;
  }

  ///////////////////////
  // UPDATE AND NOTIFY //
  ///////////////////////
  public GameState generateGameState() {
    return GameState.generateState(this);
  }

  public void preventUpdate() {
    this.preventUpdate = true;
  }

  public void enableUpdate() {
    this.preventUpdate = false;
  }

  @Override
  public void registerObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    GameState state = generateGameState();
    state.setSubmitter(submitter);
    observers.forEach(observer -> observer.update(state));
  }

  public void update(TurnStatus turnStatus, int submitter) {
    this.submitter = submitter;
    isTurnStart = false;
    hasWinner();
    if (gameMode == GameMode.TURN_BASED) {
      handleTurnStatus(turnStatus);
    }
    if (players[controlledPlayer].status() == PlayerStatus.START) {
      if (players[controlledPlayer].hand().getScore() > initialScore) {
        initialScore = players[controlledPlayer].hand().getScore();
      }
      if (initialScore - players[controlledPlayer].hand().getScore() >= 30) {
        players[controlledPlayer].setStatus(PlayerStatus.ICE_BROKEN);
      }
    }
    if (!preventUpdate) {
      notifyObservers();
    }
  }

  private void handleTurnStatus(TurnStatus turnStatus) {
    this.setTurnStatus(turnStatus);
    if (turnStatus.goNextTurn) {
      PlayerStatus status = turnStatus.isIceBroken ? PlayerStatus.ICE_BROKEN : PlayerStatus.START;
      players[currentPlayer].setStatus(status);
      nextTurn();
    } else if (turnStatus.tryEndTurn) {
      PlayerStatus status = turnStatus.isIceBroken ? PlayerStatus.ICE_BROKEN : PlayerStatus.START;
      players[currentPlayer].setStatus(status);
      tryEndTurn();
    } else if (turnStatus.isTurnEnd) {
      endTurn();
    }
  }

  private void hasWinner() {
    int winner = getWinner();
    if (winner != -1) {
      this.isGameEnd = true;
      this.winnerName = players[winner].getName();
      endGame();
      notifyObservers();
    }
  }

  public void resetHighlight() {
    for (Player player : players) {
      player.resetForNewTurn();
    }
    table.resetForNewTurn();
    status = GameStatus.SILENT;
  }
}
