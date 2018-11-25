package project.rummy.networks;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import project.rummy.messages.ConnectionData;
import project.rummy.messages.LobbyMessage;
import project.rummy.messages.PlayerInfo;
import project.rummy.messages.StringMessage;

public class NetworkGameManager {
  private ChannelGroup channels;
  private final int MAX_PLAYERS = 1;
  private PlayerInfo[] playersInfo;
  private int nextPlayer;

  public static NetworkGameManager INSTANCE;

  public static NetworkGameManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NetworkGameManager();
    }
    return INSTANCE;
  }

  private NetworkGameManager() {
    channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    playersInfo = new PlayerInfo[1];
    nextPlayer = 0;
  }

  public void onChannelConnected(Channel channel) {
    if(channels.size() < MAX_PLAYERS) {
      initChannel(channel);
    } else {
      channel.writeAndFlush(new StringMessage("The lobby is full")).addListener(ChannelFutureListener.CLOSE);
    }
  }

  public void initChannel(Channel channel) {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(
        new ObjectEncoder(),
        new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
        new MessageFilterHandler(),
        new MessageHandler());
  }

  public void onConnectionDataReceived(Channel channel, ConnectionData data) {
    channels.add(channel);
    playersInfo[nextPlayer] = new PlayerInfo(channel.id(), data);
    channel
        .writeAndFlush(playersInfo[nextPlayer++].getApprovedMessage())
        .addListener(channelFuture -> notifyPlayerConnected());
  }

  private void notifyPlayerConnected() {
    channels.writeAndFlush(new LobbyMessage(playersInfo));
  }
}
