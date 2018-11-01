package project.rummy.entities;

public enum PlayerStatus {
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
