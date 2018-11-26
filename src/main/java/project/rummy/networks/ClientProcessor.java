package project.rummy.networks;

import io.netty.channel.Channel;
import project.rummy.game.GameState;
import project.rummy.game.GameStatus;
import project.rummy.messages.ConnectionData;
import project.rummy.messages.MessageProcessor;
import project.rummy.messages.PlayerInfo;

public class ClientProcessor implements MessageProcessor {
  private ClientGameManager manager;

  public ClientProcessor(ClientGameManager manager) {
    this.manager = manager;
  }

  @Override
  public void processConnection(Channel channel, ConnectionData data) {
    if (data.isApproved()) {
      manager.onLobbyJoined(channel.id(), data.getPlayerId(), data.getName());
    } else {
      channel.close();
    }
  }

  @Override
  public void processGameState(Channel channel, GameState state) {
    System.out.println("Received GameState");
    if (state.getGameStatus() == GameStatus.RUNNING || state.getGameStatus() == GameStatus.TURN_END) {
      manager.onGameStateUpdated(state);
    } else if (state.getGameStatus() == GameStatus.STARTING){
      manager.initializeGame(state);
    }
  }

  @Override
  public void processString(Channel channel, String message) {
    System.out.println("Oh, A string: " + message);
  }

  @Override
  public void processLobbyInfo(Channel channel, PlayerInfo[] playerInfos) {
    manager.onLobbyUpdated(playerInfos);
  }


}
