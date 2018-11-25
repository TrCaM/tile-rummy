package project.rummy.messages;

import io.netty.channel.Channel;

public class ConnectionMessage extends Message {

  public ConnectionMessage(ConnectionData data) {
    super(data);
  }

  @Override
  public void handleBy(Channel channel, MessageProcessor processor) {
    if (payload instanceof ConnectionData) {
      ConnectionData data = (ConnectionData) payload;
      processor.processConnection(channel, data);
    } else {
      throw new IllegalStateException("Payload should be of type ConnectionData");
    }
  }
}
