package project.rummy.networks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import project.rummy.messages.Message;

public class MessageHandler extends SimpleChannelInboundHandler<Message> {
  private ServerProcessor serverProcessor;
  private NetworkGameManager gameManager = NetworkGameManager.getInstance();

  public MessageHandler() {
    this.serverProcessor = new ServerProcessor(gameManager);
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
