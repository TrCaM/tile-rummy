package project.rummy.networking.messages;

import java.io.Serializable;

public enum MessageType implements Serializable {
  HANDSHAKE, GAMESTATE, COMMAND
}
