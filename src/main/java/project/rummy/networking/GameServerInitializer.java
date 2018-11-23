package project.rummy.networking;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import project.rummy.game.Game;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

  private final Game game;

  public GameServerInitializer(Game game) {
    this.game = game;
  }

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(
        new ObjectEncoder(),
        new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
        new MessageFilterHandler(),
        new MessageHandler());
  }
}
