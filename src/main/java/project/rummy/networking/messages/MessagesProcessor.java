package project.rummy.networking.messages;

import io.netty.channel.Channel;
import project.rummy.game.GameState;


public interface MessagesProcessor {
  void processConnection(Channel channel, ConnectionData data);
  void processGameState(Channel channel, GameState state);
  void processString(Channel channel, String message);
}
