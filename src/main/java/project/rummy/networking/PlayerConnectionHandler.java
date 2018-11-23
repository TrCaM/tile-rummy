package project.rummy.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

public class PlayerConnectionHandler extends ChannelInboundHandlerAdapter {

  private final GameState state;

  public PlayerConnectionHandler(GameState state) {
    this.state = state;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    // Echo back the received object to the client.
    Tile tile = (Tile) msg;
    System.out.println(tile.toString());
    ctx.write(state);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
