package project.rummy.game;

import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.List;

public class PlayerTileValue {
    private int player;
    private int totalValue;

    public PlayerTileValue(int player) {
        this.player = player;
        this.totalValue = 0;
    }

    public void setTotalValue(List<Tile> tiles) {
        int total = 0;

        for (Tile tile: tiles) {
            total += tile.value();
        }
        this.totalValue = total;
    }

    public boolean isValidPlayer() {
        if (this.player >= 0 && this.player < 4) {
            return true;
        }
        return false;
    }

    public int getTotalValue() {
        return this.totalValue;
    }

    public int getPlayer() {
        return this.player;
    }
}
