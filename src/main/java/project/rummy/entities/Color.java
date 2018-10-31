package project.rummy.entities;

/**
 * An enum to indicate different card colors
 */
public enum Color {
  RED {
    public int value = 0;
  }, BLACK {
    public int value = 1;
  }, GREEN {
    public int value = 2;
  }, ORANGE {
    public int value = 3;
  };

  private int value;

  public int value() {
    return this.value;
  }
}
