package project.rummy.networks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import project.rummy.messages.Message;

public class MessageFilterHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    // We only receive Message Object and ignore other
    if (msg instanceof Message) {
      ctx.fireChannelRead(msg);
    } else {
      ctx.fireExceptionCaught(new IllegalStateException("Message should be of type Message only"));
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
