package project.rummy.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import project.rummy.commands.CommandProcessor;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;

import java.net.InetSocketAddress;

/**
 * Code adapted from:
 * https://github.com/meiry/multiplayer_cocos2dx-js_using_netty_websockets/blob/master/Server/CardWarNettyServer/src/com/server/WebSocketServer.java
 */
public class WebSocketServer {
  private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup group = new NioEventLoopGroup();

  private GameStore gameStore;
  private Game game;
  private Channel channel;

  private void initGame() {
    gameStore = new GameStore(new DefaultGameInitializer());
    game = gameStore.initializeGame();
  }

  public ChannelFuture start(int address) throws InterruptedException {
    ServerBootstrap bootstrap  = new ServerBootstrap();
    initGame();
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

  protected ChannelInitializer<SocketChannel> createInitializer() {
    return new GameServerInitializer(game);
  }

  public void destroy() {
    if (channel != null) {
      channel.close();
    }
    channelGroup.close();
    group.shutdownGracefully();
  }

}
