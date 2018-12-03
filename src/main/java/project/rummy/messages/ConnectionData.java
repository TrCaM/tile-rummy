package project.rummy.messages;

import io.netty.channel.ChannelId;

import java.io.Serializable;

public class ConnectionData implements Serializable {
  private String name;
  private int playerId;
  private ChannelId channelId;
  private boolean isApproved;
  private boolean isDisconnecting;

  public ConnectionData(String name, int id, ChannelId channelId) {
    this.name = name;
    this.channelId = channelId;
    this.playerId = id;
    this.isDisconnecting = false;
  }

  ConnectionData(String name, int id, ChannelId channelId, boolean isApproved) {
    this.name = name;
    this.playerId = id;
    this.channelId = channelId;
    this.isApproved = isApproved;
    this.isDisconnecting = !isApproved;
  }

  public String getName() {
    return name;
  }

  public int getPlayerId() {
    return playerId;
  }

  public boolean isApproved() {
    return isApproved;
  }

  public boolean isDisconnecting() {
    return isDisconnecting;
  }

  public ChannelId getChannelId() {
    return channelId;
  }
}
