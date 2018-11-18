package project.rummy.game.GameReader;

import com.google.gson.*;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import project.rummy.game.LoadGameInitializer;

import java.io.*;
import java.util.List;

public class WriteGameState {

    protected void writeToJSON(JSONObject object) throws  IOException {
        FileWriter writer = new FileWriter("load\\test1.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = object.toJSONString();
        JsonElement parser = new JsonParser().parse(str);

        String st =gson.toJson(parser);
        writer.write(st);
        writer.close();
    }

    protected JSONArray writeMelds(List<Meld> melds) {
        JSONArray meld = new JSONArray();
        JSONArray jsonMelds = new JSONArray();
        JSONObject object = new JSONObject();
        for (Meld item: melds) {
            for (Tile tiles: item.tiles()) {
                meld.add(tiles.color().toString().charAt(0) +"" +tiles.value());
            }
            object.put(FileLoadTypes.Meld.name(), meld);
            jsonMelds.add(object);

            meld = new JSONArray();
            object = new JSONObject();
        }
        return jsonMelds;
    }

    protected JSONObject writePlayer(HandData data, PlayerStatus status, PlayerData pData) {
        JSONArray tiles = new JSONArray();
        JSONObject object = new JSONObject();

        for (Tile getTiles: data.tiles) {
            tiles.add(getTiles);
        }
        object.put(FileLoadTypes.Tiles.name() , tiles);
        object.put(FileLoadTypes.Name.name(), pData.name);
        object.put(FileLoadTypes.Status.name(), status.toString());
        object.put(FileLoadTypes.Controller.name(), pData.controllerType);


        return object;

    }

    protected JSONArray getFreeTiles(GameState state) {
        JSONArray tiles = new JSONArray();
        for (Tile tile: state.getTableData().freeTiles) {
            tiles.add(tile);
        }
        return tiles;
    }


}
