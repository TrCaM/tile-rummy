package project.rummy.messages;

import io.netty.channel.Channel;

public class StringMessage extends Message {

  public StringMessage(String message) {
    super(message);
  }

  @Override
  public void handleBy(Channel channel, MessageProcessor processor) {
    if (payload instanceof String) {
      processor.processString(channel, (String) payload);
    } else {
      throw new IllegalStateException("Payload should be of String class");
    }
  }
}
