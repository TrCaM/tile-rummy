package project.rummy.game;

import project.rummy.entities.Table;
import project.rummy.entities.Player;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class Game implements Observable {
  private Player[] players;
  private Table table;
  private int currentPlayer;
  private List<Observer> observers;
  private int turnNumber;

  Game(Player[] players, Table table) {
    this.players = players;
    this.table = table;
    this.observers = new ArrayList<>();
    this.turnNumber = 0;
    this.currentPlayer = 0;
  }

  /**
   * Start the game specifically:
   *  + It create the game loops to keep looping until the game is done
   *  + Each loop will represent a single move that requires the interface update
   *  + Redraw the components that need to be re-rendered after each iteration
   *  + Constantly check the state of the game and check for when the game should end
   */
  public void start() {
  }

  /**
   * Check the current state of the game to see that if the game has ended:
   *  + When a player has win the game (no tile in hand)
   *  + When there's no free tile to draw.
   * @return the player index who won the game. That player either is the only player who has no
   *         tile or has the least points in hand when there's no tile to draw and play.
   */
  public int isGameEnd() {
    //TODO: Write test and implement the method
    throw new UnsupportedOperationException();
  }
  Table getTable() {
    return this.table;
  }

  Player[] getPlayers() {
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

  private GameState generateGameState() {
    return GameState.generateState(this);
  }
}
