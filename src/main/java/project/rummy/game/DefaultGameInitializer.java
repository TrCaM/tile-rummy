package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.strategies.AggressiveStrategy;
import project.rummy.strategies.ModerateStrategy;
import project.rummy.strategies.PassiveStrategy;

public class DefaultGameInitializer implements GameInitializer {

  @Override
  public Player[] initPlayers() {
    Controller[] controllers = new Controller[]{
        new ManualController(),
        new AutoController(new AggressiveStrategy()),
        new AutoController(new PassiveStrategy()),
        new AutoController(new ModerateStrategy())};
    Player[] players = new Player[4];
    for (int i=0; i<4;i++) {
      players[i] = new Player(controllers[i]);
    }
    return players;
  }

  @Override
  public Table initTable() {
    return null;
  }

  @Override
  public GameStatus initGameStatus() {
    return null;
  }
}
