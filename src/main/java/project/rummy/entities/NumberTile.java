package project.rummy.entities;

import java.util.*;

public class NumberTile extends Tile {

  NumberTile(Color color, int value) {
    super(color);
    this.value = value;
  }

  @Override
  public boolean isJoker() {
    return false;
  }

  @Override
  public boolean canFillToRun(Color color, int value) {
    return this.color == color && this.value == value;
  }

  @Override
  public boolean canFillToSet(Set<Color> existingcolor, int value) {
    return existingcolor.contains(this.color) && this.value == value;
  }

  @Override
  public String toString() {
    return String.format("%s%d", color.toString().charAt(0), value);
  }

  @Override
  public String toSymbol() {
    return String.valueOf(value);
  }
}
