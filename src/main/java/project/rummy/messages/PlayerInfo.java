package project.rummy.messages;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.io.Serializable;

/**
 * This class stores all information about the players who connect to the game
 */
public class PlayerInfo implements Serializable {
  private String name;
  private int playerId;

  private ChannelId channelId;

  public PlayerInfo(ChannelId id, ConnectionData data) {
    this.channelId = id;
    this.name = data.getName();
    this.playerId = data.getPlayerId();
  }

  public String getName() {
    return name;
  }

  public int getPlayerId() {
    return playerId;
  }

  public ChannelId getChannelId() {
    return channelId;
  }

  public ConnectionMessage getApprovedMessage() {
    ConnectionData data = new ConnectionData(name, playerId, channelId, true);
    return new ConnectionMessage(data);
  }

  public ConnectionMessage getDisconnectMessage() {
    ConnectionData data = new ConnectionData(name, playerId, channelId, false);
    return new ConnectionMessage(data);
  }
}
