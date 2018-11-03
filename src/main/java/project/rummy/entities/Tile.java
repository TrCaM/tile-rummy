package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

/**
 * Represents tiles in the game
 */
public class Tile extends Component {
  private final Color color;
  private final int value;

  private boolean hightlight;

  public Tile(Color color, int value) {
    this.color = color;
    this.value = value;
    this.hightlight = false;
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

  public void setHightlight(boolean hightlight) {
    this.hightlight = hightlight;
  }

  public boolean isHightlight() {
    return hightlight;
  }
  @Override
  public String toString() {
    return String.format("%s %d", color.toString(), value);
  }
}
