package project.rummy.networks;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

public class GameClientTask {
  private static final String HOST = System.getProperty("host", "127.0.0.1");
  private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
  private final String playerName;
  private ClientGameManager manager;

  public GameClientTask(String playerName, ClientGameManager gameManager) {
    this.playerName = playerName;
    this.manager = gameManager;
  }

  public Mono<Channel> connectToServer() {
    return Mono.fromCallable(() -> {
      EventLoopGroup group = new NioEventLoopGroup();
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(HOST, PORT))
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              ChannelPipeline p = ch.pipeline();
              p.addLast(
                  new ObjectEncoder(),
                  new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
                  new MessageFilterHandler(),
                  new ClientMessageHandler(playerName, new ClientProcessor(manager)));
            }
          });
      ChannelFuture f = b.connect();
      Channel chn = f.channel();
      f.sync();
      return chn;
    });
  }
}
