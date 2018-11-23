package project.rummy.networking.messages;


import io.netty.channel.Channel;

public class ConnectionMessage extends Message {

  public ConnectionMessage(int id, ConnectionData data) {
    super(id, data);
  }

  @Override
  public void handleBy(Channel channel, MessagesProcessor processor) {
    if (payload instanceof ConnectionData) {
      ConnectionData data = (ConnectionData) payload;
      processor.processConnection(channel, data);
    } else {
      throw new IllegalStateException("Payload should be of type ConnectionData");
    }
  }
}
