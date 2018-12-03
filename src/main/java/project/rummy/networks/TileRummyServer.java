package project.rummy.networks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.net.InetSocketAddress;

public class TileRummyServer {
  private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup group = new NioEventLoopGroup();

  private Channel channel;

  public ChannelFuture start(int address) throws InterruptedException {
    ServerBootstrap bootstrap  = new ServerBootstrap();
    bootstrap.group(group)
        .channel(NioServerSocketChannel.class)
        .childHandler(createInitializer())
        .option(ChannelOption.SO_BACKLOG, 4)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
    ChannelFuture future = bootstrap.bind(new InetSocketAddress(address));
    future.sync();
    channel = future.channel();
    return future;
  }

  private ChannelInitializer<SocketChannel> createInitializer() {
    return new GameServerInitializer();
  }

  public void destroy() {
    if (channel != null) {
      channel.close();
    }
    channelGroup.close();
    group.shutdownGracefully();
  }
}
