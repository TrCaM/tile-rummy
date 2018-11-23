package project.rummy.entities;

import java.io.Serializable;

/**
 * An enum to indicate different card colors
 */
public enum Color implements Serializable {
  //TODO: Change ANY that has value 0
  ANY(4), RED(0), BLACK(1), GREEN(2), ORANGE(3);

  private int value;

  public int value() {
    return this.value;
  }

  Color(int value){
    this.value =value;
  }
}
