package project.rummy.networking.messages;

import io.netty.channel.Channel;

import java.io.Serializable;

public abstract class Message implements Serializable {
  int channelId;
  Object payload;

  public Message(int id, Object payload) {
    this.channelId = id;
    this.payload = payload;
  }

  public abstract void handleBy(Channel channel, MessagesProcessor processor);

  public int getChannelId() {
    return channelId;
  }

  public Object getPayload() {
    return payload;
  }
}
