package project.rummy.entities;

import java.io.Serializable;

public enum PlayerStatus implements Serializable {
  START("NOT PLAYED"), ICE_BROKEN("ICE BROKEN");

  private String note;

  PlayerStatus(String note) {
    this.note = note;
  }


  @Override
  public String toString() {
    return note;
  }
}
