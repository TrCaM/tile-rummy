package project.rummy.networks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import project.rummy.messages.ConnectionData;
import project.rummy.messages.ConnectionMessage;
import project.rummy.messages.Message;
import project.rummy.messages.MessageProcessor;

public class ClientMessageHandler extends SimpleChannelInboundHandler<Message> {
  private MessageProcessor processor;
  private String playerName;

  public ClientMessageHandler(String playerName, MessageProcessor processor) {
    this.processor = processor;
    this.playerName = playerName;
  }



  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
    message.handleBy(channelHandlerContext.channel(), processor);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println(cause.getMessage());
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ConnectionData connectionData = new ConnectionData(playerName, 0);
    ctx.writeAndFlush(new ConnectionMessage(connectionData));
  }
}

