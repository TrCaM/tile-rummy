package project.rummy.networks;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel channel) {
    NetworkGameManager.getInstance().onChannelConnected(channel);
  }
}
