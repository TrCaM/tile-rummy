package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Player;
import project.rummy.entities.PlayerData;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;
import project.rummy.strategies.Strategy4;

import java.util.List;

public class CustomGameInitializer extends DefaultGameInitializer {
  private List<PlayerData> playerDataList;

  public CustomGameInitializer(List<PlayerData> playerDataList) {
    super();
    this.playerDataList = playerDataList;
  }

  @Override
  public void initPlayers(Game game) {
    Controller controller;
    Player[] players = new Player[playerDataList.size()];
    for (int i = 0; i < playerDataList.size(); i++) {
      switch (playerDataList.get(i).controllerType) {
        case "human":
          controller = new ManualController();
          game.setControlledPlayer(i);
          break;
        case "strategy1":
          controller = new AutoController(game, new Strategy1());
          break;
        case "strategy2":
          controller = new AutoController(game, new Strategy2());
          break;
        case "strategy3":
          controller = new AutoController(game, new Strategy3());
          break;
        case "strategy4":
          controller = new AutoController(game, new Strategy4());
          break;
        default:
          throw new IllegalStateException("Player type should be human or a strategy");
      }
      players[i] = new Player(playerDataList.get(i).name, controller, i);
    }
    game.setUpPlayers(players);
  }
}
