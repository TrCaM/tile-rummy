package project.rummy.messages;

import io.netty.channel.Channel;
import project.rummy.game.GameState;


public interface MessageProcessor {
  void processConnection(Channel channel, ConnectionData data);

  void processGameState(Channel channel, GameState state);

  void processString(Channel channel, String message);

  void processLobbyInfo(Channel channel, PlayerInfo[] playerInfos);
}
