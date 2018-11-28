package project.rummy.networks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import project.rummy.messages.Message;
import project.rummy.messages.MessageProcessor;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {
  private MessageProcessor processor;
  private NetworkGameManager gameManager = NetworkGameManager.getInstance();

  public MessageHandler(MessageProcessor processor) {
    this.processor = processor;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
    message.handleBy(channelHandlerContext.channel(), processor);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    gameManager.onChannelDisconnect(ctx.channel());
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println(cause.getMessage());
  }
}
