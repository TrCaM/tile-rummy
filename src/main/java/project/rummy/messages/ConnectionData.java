package project.rummy.messages;

import java.io.Serializable;

public class ConnectionData implements Serializable {
  private String name;
  private int playerId;
  private boolean isApproved;

  public ConnectionData(String name, int id) {
    this.name = name;
    this.playerId = id;
  }

  public ConnectionData(String name, int id, boolean isApproved) {
    this.name = name;
    this.playerId = id;
    this.isApproved = isApproved;
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

  public boolean isReady() {
    return isReady;
  }

  private boolean isReady;

  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }
}
