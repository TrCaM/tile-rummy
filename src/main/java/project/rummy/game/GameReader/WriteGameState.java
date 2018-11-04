package project.rummy.game.GameReader;

import com.google.gson.*;
import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.PlayerData;
import project.rummy.entities.Tile;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;

public class WriteGameState {
    private GameState gameState;

    // this is the purposed changes to the write game... not fully implemented... yet.s

    public WriteGameState(GameState gameState) {
        this.gameState = gameState;

    }

    public boolean write() throws IOException {
        // this writes to a JSON File, this should probably be implemented as a button
        // This uses the google
        JSONObject gameInfo = new JSONObject();
        JSONArray players = new JSONArray();
        HandData data[] = gameState.getHandsData();

        gameInfo = writePlayer();
        writeToJSON(gameInfo);

        return false;
    }

    private void writeToJSON(JSONObject object) throws  IOException {
        FileWriter writer = new FileWriter("load\\test1.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = object.toJSONString();
        JsonElement parser = new JsonParser().parse(str);

        String st =gson.toJson(parser);
        writer.write(st);
        writer.close();
    }
    private JSONObject writePlayer() {
        JSONObject playerSample = new JSONObject();
        JSONArray player_info = new JSONArray();
        JSONObject info = new JSONObject();
        JSONArray meld_info = new JSONArray();
        JSONArray tiles = new JSONArray();


        for (int i = 0; i < gameState.getPlayerData().length; i++) {
            JSONObject melds = new JSONObject();
            melds = writeAll(i);
            playerSample.put(gameState.getPlayerData()[i].name, melds);
        }


        playerSample.put(FileLoadTypes.Id, 0);





        playerSample.put(FileLoadTypes.MeldsOnTable, writeMeldPlayed());

        playerSample.put(FileLoadTypes.Turn, gameState.getTurnNumber());
        playerSample.put(FileLoadTypes.CurrentPlayer, gameState.getCurrentPlayer());
        playerSample.put(FileLoadTypes.TileDrawn, gameState.getFreeTilesCount());
        playerSample.put(FileLoadTypes.Deck, gameState.getFreeTilesCount());
        playerSample.put(FileLoadTypes.MeldPlay, "");
        playerSample.put(FileLoadTypes.FreeTiles , tiles);


        return playerSample;
    }

    private JSONArray writeMeldPlayed() {
        JSONArray meld = new JSONArray();
        JSONArray melds = new JSONArray();
        for (Meld item: gameState.getTableData().melds) {
            for (Tile tiles: item.tiles()) {
                meld.add( tiles.color().toString().charAt(0) +"" +tiles.value());
            }
            melds.add(meld);
        }

        return melds;
    }

    private JSONObject writeAll(int player) {
        JSONObject item = new JSONObject();
        JSONArray tiles = new JSONArray();
        item.put(FileLoadTypes.Tiles, writeTiles(player));
        item.put(FileLoadTypes.Status, gameState.getPlayerStatuses()[player].toString());

        return item;
    }

    private JSONArray  writeFreeTiles() {
        JSONObject item = new JSONObject();
        JSONArray tiles = new JSONArray();
        for (Tile tile: gameState.getTableData().freeTiles) {
            tiles.add(tile.color().toString().charAt(0) + "" + tile.value());
        }

        return tiles;
    }

    private JSONArray writeTiles(int player) {
        JSONArray tiles_info = new JSONArray();
        JSONObject item = new JSONObject();
        for (Tile tile: gameState.getHandsData()[player].tiles) {
            String str = tile.color().toString();
            tiles_info.add(str.charAt(0) + "" + tile.value());
       }

        return  tiles_info;
    }

}
