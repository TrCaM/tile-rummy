package project.rummy.game;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.*;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class Game extends Component implements Observable {
  private Player[] players;
  private Table table;
  private int currentPlayer;
  private List<Observer> observers;
  private int turnNumber;
  private CommandProcessor commandProcessor;
  private boolean isGameEnd;
  private String winnerName;
  TurnStatus turnStatus;
  private boolean preventUpdate;
  private int controlledPlayer;
  private GameStatus status;
  private boolean isNetworkGame;
  private  boolean isTurnStart;

  Game(boolean isNetworkGame) {
    super();
    this.observers = new ArrayList<>();
    this.commandProcessor = CommandProcessor.getInstance();
    this.preventUpdate = false;
    this.status = GameStatus.NOT_STARTED;
    this.isNetworkGame = isNetworkGame;
  }

  public void setUpPlayer(Player[] players) {
    this.players = players;
    this.turnNumber = 0;
    this.currentPlayer = 0;
  }

  public void setStatus(GameStatus status) {
    this.status = status;
  }

  public void setControlledPlayer(int index) {
    this.controlledPlayer = index;
  }

  public Player getControlledPlayer() {
    return players[controlledPlayer];
  }

  public void setUpTable(Table table) {
    this.table = table;
  }

  public void setTurnNumber(int num) {
    this.turnNumber = num;
  }

  public Player getCurrentPlayerObject() {
    return players[currentPlayer];
  }

  /**
   * Start the game specifically:
   * + It create the game loops to keep looping until the game is done
   * + Each loop will represent a single move that requires the interface update
   * + Redraw the components that need to be re-rendered after each iteration
   * + Constantly check the state of the game and check for when the game should end
   */
  public void tryEndTurn() {
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
    resetTileHightlight();
    this.status = GameStatus.RUNNING;
    currentPlayer = (currentPlayer + 1) % 4;
    playTurn();
  }

  public void startGame(boolean reset) {
    if (reset) {
      initGameTable();
    }
    turnNumber = 1;
    this.status = GameStatus.RUNNING;
    playTurn();
  }



  public void startGame() {
    startGame(false);
  }

  public void initGameTable() {
    GameInitializer initializer = new DefaultGameInitializer();
    initializer.initTable(this);
    initializer.initializeGameState(players, table);
  }

  private void playTurn() {
    ActionHandler handler = new ActionHandler(players[currentPlayer], table);
    commandProcessor.setUpHandler(handler);
    this.turnStatus = handler.getTurnStatus();
    handler.backUpTurn();
    this.players[currentPlayer].getController().playTurn();
    isTurnStart = true;
    notifyObservers();
  }

//  private void setGameStatusForNextTurn() {
//    if (controlledPlayer == currentPlayer) {
//      this.status = GameStatus.TURN_END;
//    } else {
//      this.status = GameStatus.RUNNING;
//    }
//  }

  public void endTurn() {
    this.players[currentPlayer].getController().closeInput();
    commandProcessor.reset();
  }

  private void resetTileHightlight() {
    players[currentPlayer].resetForNewTurn();
    table.resetForNewTurn();
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public void restoreTurn(Hand hand, Table table) {
    players[currentPlayer].setHand(hand);
    this.table = table;
  }

  /**
   * Check the current state of the game to see that if the game has ended:
   * + When a player has win the game (no tile in hand)
   * + When there's no free tile to draw.
   *
   * @return the player index who won the game. That player either is the only player who has no
   * tile or has the least points in hand when there's no tile to draw and play.
   * Return -1 if the game is not ended.
   */
  public int getWinner() {
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
    } else {
      return -1;
    }
  }

  public boolean isNetworkGame() {
    return isNetworkGame;
  }

  public boolean isGameEnd() {
    return isGameEnd;
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
    observers.forEach(observer -> observer.update(state));
  }

  public void update(TurnStatus turnStatus) {
    isTurnStart = false;
    int winner = getWinner();
    if (winner != -1) {
      this.isGameEnd = true;
      this.winnerName = players[winner].getName();
      notifyObservers();
    }
    this.turnStatus = turnStatus;
    if (turnStatus.goNextTurn) {
      PlayerStatus status = turnStatus.isIceBroken ? PlayerStatus.ICE_BROKEN : PlayerStatus.START;
      players[currentPlayer].setStatus(status);
      nextTurn();
    } else if (turnStatus.tryEndTurn) {
      PlayerStatus status = turnStatus.isIceBroken ? PlayerStatus.ICE_BROKEN : PlayerStatus.START;
      players[currentPlayer].setStatus(status);
      tryEndTurn();
    } else if (turnStatus.isTurnEnd) {
      if (winner != -1) {
        this.isGameEnd = true;
        this.winnerName = players[winner].getName();
        notifyObservers();
      } else {
        endTurn();
      }
    }
    if (!preventUpdate) {
      notifyObservers();
    }
  }

  public void setCurrentPlayer(int player) {
    this.currentPlayer = player;
  }

  public void setTurnStatus(TurnStatus turnStatus) {
    this.turnStatus = turnStatus;
  }

  public String getWinnerName() {
    return this.winnerName;
  }

  public GameState generateGameState() {
    return GameState.generateState(this);
  }

  public void preventUpdate() {
    this.preventUpdate = true;
  }

  public void enableUpdate() {
    this.preventUpdate = false;
  }

  public GameStatus getStatus() {
    return status;
  }

  public boolean isTurnBeginning() {
    return isTurnStart;
  }
}
