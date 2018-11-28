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
  static final String HOST = System.getProperty("host", "127.0.0.1");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
  static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
  private final String playerName;
  private ClientGameManager mananger;

  public GameClientTask(String playerName, ClientGameManager gameManager) {
    this.playerName = playerName;
    this.mananger = gameManager;
  }

  public Mono<Channel> connectToServer() throws Exception {
    return Mono.fromCallable(() -> {
      EventLoopGroup group = new NioEventLoopGroup();
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .remoteAddress(new InetSocketAddress(HOST, PORT))
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline p = ch.pipeline();
              p.addLast(
                  new ObjectEncoder(),
                  new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
                  new MessageFilterHandler(),
                  new ClientMessageHandler(playerName, new ClientProcessor(mananger)));
            }
          });
      ChannelFuture f = b.connect();
      Channel chn = f.channel();
      f.sync();
      return chn;
    });
  }
}
