package project.rummy.game.GameReader;

import com.google.gson.*;
import project.rummy.entities.HandData;
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
        FileWriter writer = new FileWriter("load\\test.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = object.toJSONString();
        JsonElement parser = new JsonParser().parse(str);

        String st =gson.toJson(parser);
     //   System.out.println(st);
        writer.write(st);
        writer.close();
    }
    private JSONObject writePlayer() {
        JSONObject playerSample = new JSONObject();
        JSONArray player_info = new JSONArray();
        JSONObject info = new JSONObject();

        for (PlayerLoadTypes types: PlayerLoadTypes.values()) {
            JSONObject melds = new JSONObject();

            melds = writeAll(0, 3);
            playerSample.put(types, melds);
        }

        return playerSample;
    }

    private JSONObject writeAll(int player, int melds_amount) {
        JSONArray meld_info = new JSONArray();
        JSONObject info = new JSONObject();
        JSONObject item = new JSONObject();
        JSONArray tiles = new JSONArray();



        for (int i = 0; i < melds_amount; i++) {
        //    tiles.addAll(gameState.getHandsData()[player].melds.get(i).tiles());
            tiles.add("C8");
            tiles.add("C13");
            info.put(FileLoadTypes.meldedTile, tiles);

            info.put(FileLoadTypes.Id, 0);
            meld_info.add(info);
        }



        item.put(FileLoadTypes.Melds, meld_info);

        item.put(FileLoadTypes.Tiles, writeTiles());
        item.put(FileLoadTypes.Name, "");
        item.put(FileLoadTypes.Status, "");
        return item;
    }

    private JSONArray writeTiles() {
        JSONArray tiles_info = new JSONArray();
        tiles_info.add("C8");
        tiles_info.add("C0");

        return  tiles_info;
    }


/***
 * {
 *      Playern {
 *       meld {
 *           // times are here
 *       }
 *        status: ""
 *        name: ""
 *
 *      }
 *
 *
 *
 * }
 */
}
