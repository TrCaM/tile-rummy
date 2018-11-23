package project.rummy.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import project.rummy.networking.messages.Message;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {
  private ServerProcessor serverProcessor;

  public MessageHandler() {
    this.serverProcessor = new ServerProcessor();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
    message.handleBy(channelHandlerContext.channel(), serverProcessor);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println(cause.getMessage());
  }
}
