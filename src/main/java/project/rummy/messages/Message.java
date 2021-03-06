package project.rummy.messages;

import io.netty.channel.Channel;

import java.io.Serializable;

public abstract class Message implements Serializable {
  Object payload;

  Message(Object payload) {
    this.payload = payload;
  }

  public abstract void handleBy(Channel channel, MessageProcessor processor);

}
