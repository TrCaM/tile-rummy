package project.rummy.entities;
import java.util.*;

    public class NumberTile extends Tile {
    private boolean hightlight;

    public NumberTile(Color color, int value) {
        super (color);
        this.value = value;
    }

    public void setHightlight(boolean hightlight) {
        this.hightlight = hightlight;
    }

    public boolean isHightlight() {
        return hightlight;
    }

    @Override
    public boolean canFillToRun(int value) {
        return this.value == value;
    }

    @Override
    public boolean canFillToSet(Set<Color> existingcolor) {
        return existingcolor.contains(this.color);
    }

    @Override
    public String toString() {
        return String.format("%s%d", color.toString().charAt(0), value);
    }

// //TODO: test for compareTO
//  @Override
//  public int compareTo(@NotNull Tile tile) {
//      if(this.color() != tile.color()){
//        return this.color().compareTo(tile.color());
//      }
//      if(this.value() == 0 || tile.value() == 0){
//        return 0;
//      }
//        return  this.value() - tile.value();
//  }
}
