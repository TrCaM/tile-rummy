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
   * Start the game
   */
  public void start() {

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
