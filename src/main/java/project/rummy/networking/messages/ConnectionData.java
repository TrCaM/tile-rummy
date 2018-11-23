package project.rummy.networking.messages;

import java.io.Serializable;

public class ConnectionData implements Serializable {
  private String name;
  private int playerId;
  private int playerNumber;
  private boolean isApproved;

  public ConnectionData(String name, int id) {
    this.name = name;
    this.playerId = id;
  }

  public ConnectionData(String name, int id, int playerNumber, boolean isApproved) {
    this.name = name;
    this.playerId = id;
    this.playerNumber = playerNumber;
    this.isApproved = isApproved;
  }

  public String getName() {
    return name;
  }

  public int getPlayerId() {
    return playerId;
  }

  public int getPlayerNumber() {
    return playerNumber;
  }

  public boolean isApproved() {
    return isApproved;
  }

  public boolean isReady() {
    return isReady;
  }

  private boolean isReady;
}
