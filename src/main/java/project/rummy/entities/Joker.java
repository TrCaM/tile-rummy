package project.rummy.entities;

import java.util.Set;

public class Joker extends  Tile {

    public Joker() {
        super();
        this.value = 30;
    }

    @Override
    public boolean canFillToRun(int value) {
        return true;
    }

    @Override
    public boolean canFillToSet(Set<Color> existingColor) {
        return true;
    }
}
