package project.rummy.networking;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;
import project.rummy.networking.messages.ConnectionData;
import project.rummy.networking.messages.ConnectionMessage;
import project.rummy.networking.messages.GameStateMessage;
import project.rummy.networking.messages.MessagesProcessor;

public class ServerProcessor implements MessagesProcessor {
  private ChannelGroup channels;
  private GameStore store;
  private boolean isGameStarted;
  private final int MAX_CLIENTS = 1;

  public ServerProcessor() {
    channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    store = new GameStore(new DefaultGameInitializer());
    this.isGameStarted = false;
  }

  @Override
  public void processConnection(Channel channel, ConnectionData data) {
    if (!isGameStarted) {
      channels.add(channel);
      isGameStarted = true;
      Game game = store.initializeGame();
      channels.writeAndFlush(new GameStateMessage(0 , game.generateGameState()));
    } else {
      ConnectionData refuseData =
          new ConnectionData(data.getName(), data.getPlayerId(), -1, false);
      channel.writeAndFlush(new ConnectionMessage(0, refuseData));
    }
  }

  @Override
  public void processGameState(Channel channel, GameState state) {
    System.out.println("Received GameState");
  }

  @Override
  public void processString(Channel channel, String message) {
    System.out.println("Oh, A string: " + message);
  }
}
