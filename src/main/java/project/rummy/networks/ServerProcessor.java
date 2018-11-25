package project.rummy.networks;

import io.netty.channel.Channel;
import project.rummy.game.GameState;
import project.rummy.messages.ConnectionData;
import project.rummy.messages.MessageProcessor;
import project.rummy.messages.PlayerInfo;

public class ServerProcessor implements MessageProcessor {

  private NetworkGameManager gameManager;

  ServerProcessor(NetworkGameManager gameManager) {
    this.gameManager = gameManager;
  }

  @Override
  public void processConnection(Channel channel, ConnectionData data) {
    gameManager.onConnectionDataReceived(channel, data);
  }

  @Override
  public void processGameState(Channel channel, GameState state) {
    System.out.println("Received GameState");
  }

  @Override
  public void processString(Channel channel, String message) {
    System.out.println("Oh, A string: " + message);
  }

  @Override
  public void processLobbyInfo(Channel channel, PlayerInfo[] playerInfos) {
    System.out.println("Server does not handle this type of message");
  }
}
