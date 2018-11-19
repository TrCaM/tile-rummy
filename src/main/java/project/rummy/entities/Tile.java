package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.Set;

/**
 * Represents tiles in the game
 */
public abstract class Tile extends Component {
    protected final Color color;
    protected  int value;

    private boolean hightlight;

    public Tile(Color color) {
        this.color = color;
        this.hightlight = false;
    }

    public static Tile createTile(Color color, int value) {
        //TODO check if value is 0 , yes => create Joker
        if (value == 0) {
            return new Joker(color);
        }
        return new NumberTile(color, value);
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

    public abstract boolean canFillToRun(int value);

    public abstract boolean canFillToSet(Set<Color> existingcolor);

}
