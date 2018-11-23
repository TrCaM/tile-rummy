package project.rummy.networking.messages;

import io.netty.channel.Channel;

public class StringMessage extends Message {

  public StringMessage(int id, String message) {
    super(id, message);
  }

  @Override
  public void handleBy(Channel channel, MessagesProcessor processor) {
    if (payload instanceof String) {
      processor.processString(channel, (String) payload);
    } else {
      throw new IllegalStateException("Payload should be of String class");
    }
  }
}
