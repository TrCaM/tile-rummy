package project.rummy.entities;

import java.util.Set;

public class Joker extends Tile {

  Joker() {
    super();
    this.value = 30;
  }

  @Override
  public boolean isJoker() {
    return true;
  }

  @Override
  public boolean canFillToRun(Color color, int value) {
    return true;
  }

  @Override
  public boolean canFillToSet(Set<Color> existingColor, int value) {
    return true;
  }

  @Override
  public String toSymbol() {
    return "J";
  }
}
