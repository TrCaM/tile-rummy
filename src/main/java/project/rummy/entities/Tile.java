package project.rummy.entities;

/**
 * Represents tiles in the game
 */
public class Tile {
  private final Color color;
  private final int value;

  private Tile(Color color, int value) {
    this.color = color;
    this.value = value;
  }

  public static Tile createTile(Color color, int value) {
    return new Tile(color, value);
  }

  public Color color() {
    return this.color;
  }

  public int value() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.format("%s %d", color.toString(), value);
  }
}
