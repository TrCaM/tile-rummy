package project.rummy.networks;

import io.netty.channel.ChannelId;
import project.rummy.entities.Meld;
import project.rummy.game.*;
import project.rummy.main.TileRummyApplication;
import project.rummy.messages.GameStateMessage;
import project.rummy.messages.PlayerInfo;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class ClientGameManager implements Observable, Observer {
  private TileRummyApplication gameApplication;
  private int playerId;
  private String playerName;
  private ChannelId channelId;
  private boolean isGameStarted;
  private List<Observer> observers;
  private GameState gameState;

  public ClientGameManager() {
    this.gameApplication = TileRummyApplication.getInstance();
    this.observers = new ArrayList<>();
  }

  void onLobbyJoined(ChannelId channelId, int playerId, String playerName) {
    this.channelId = channelId;
    this.playerId = playerId;
    this.playerName = playerName;
  }

  void onLobbyUpdated(PlayerInfo[] playerInfos) {
    for (int i = 0; i < playerInfos.length; i++) {
      System.out.println("Connected " + playerInfos[i].getName());
      if (playerInfos[i].getChannelId().equals(channelId)) {
        playerId = i;
        playerName = playerInfos[i].getName();
      }
    }
    System.out.println();
  }

  void initializeGame(GameState initialState) {
    gameApplication.startNetworkGame(initialState, playerId);
    isGameStarted = true;
  }

  void onGameStateUpdated(GameState state) {
    this.gameState = state;
    Meld.cleanUpMap(state);
    Meld.syncMeldId(state.getNextMeldId());
    notifyObservers();
  }

  void endGame() {
    System.exit(0);
  }

  public boolean isGameStarted() {
    return isGameStarted;
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
    observers.forEach(observer -> observer.update(gameState));
  }

  @Override
  public void update(GameState status) {
    if (isGameStarted && (playerId == status.getCurrentPlayer())) {
      gameApplication.getChannel().writeAndFlush(new GameStateMessage(status));
    }
  }
}
