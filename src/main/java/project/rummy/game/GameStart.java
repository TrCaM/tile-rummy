package project.rummy.game;

import project.rummy.entities.Hand;
import project.rummy.entities.Player;
import project.rummy.entities.Tile;
import project.rummy.game.GameReader.PlayerLoadTypes;

import java.util.ArrayList;

public class GameStart {
    private Game game;
    private PlayerTileValue playerTileValue[];
    private ArrayList<Integer> indexPlayers;

    public GameStart(Game game) {
        this.game = game;
        this.playerTileValue = new PlayerTileValue[4];
    }


    public int getPlayerValue() {
        int player = -1;
        int highest = 0;
        boolean playersAdded = false;
        indexPlayers = new ArrayList<>();

        for (int i = 0; i < this.game.getPlayers().length; i++) {
            this.playerTileValue[i] = new PlayerTileValue(i);
            this.playerTileValue[i].setTotalValue(this.game.getPlayers()[i].hand().getTiles());
        }

        for (int i = 0; i < playerTileValue.length; i++) {
            if (highest < playerTileValue[i].getTotalValue()) {
                indexPlayers = new ArrayList<>();
                highest = playerTileValue[i].getTotalValue();
                player = playerTileValue[i].getPlayer();
            }
            else if (highest == playerTileValue[i].getTotalValue()) {
                indexPlayers.add(playerTileValue[i].getPlayer());

                if (!playersAdded) {
                    indexPlayers.add(player);
                    playersAdded = true;
                }
            }
        }

        if (!indexPlayers.isEmpty() ) {
            for (int i = 0; i < this.game.getPlayers().length; i++) {
                for (Integer index: indexPlayers) {

                    if (i == index) {
                        this.game.getPlayers()[i].hand().addTiles(this.game.getTable().drawTile());
                    }
                }
            }
            player = getPlayerValue();
        }


        return player;
    }
}
