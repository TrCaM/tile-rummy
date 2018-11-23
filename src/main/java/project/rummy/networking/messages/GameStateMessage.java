package project.rummy.networking.messages;

import io.netty.channel.Channel;
import project.rummy.game.GameState;


public class GameStateMessage extends Message {

  public GameStateMessage(int id, GameState gameState) {
    super(id, gameState);
  }

  @Override
  public void handleBy(Channel channel, MessagesProcessor processor) {
    if (payload instanceof GameState) {
      GameState state = (GameState) payload;
      processor.processGameState(channel, state);
    } else {
      throw new IllegalStateException("Payload is not of type GameState");
    }
  }
}
