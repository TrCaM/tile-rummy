package project.rummy.messages;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

/**
 * This class stores all information about the players who connect to the game
 */
public class PlayerInfo {
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
    ConnectionData data = new ConnectionData(name, playerId, true);
    return new ConnectionMessage(data);
  }
}
