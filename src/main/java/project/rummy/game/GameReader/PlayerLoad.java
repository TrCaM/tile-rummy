package project.rummy.game.GameReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class PlayerLoad {
    private String status;
    private List<String> tiles;
    private String name;

    public PlayerLoad(Object playerInfoRaw) {
        JSONObject item = (JSONObject)playerInfoRaw;
        JsonElement parser = new JsonParser().parse(item.toJSONString());
        JsonObject jsonObject = parser.getAsJsonObject();
        tiles = new ArrayList<>();
        List<String> meldtiles;

        //single items
        this.name = jsonObject.get(FileLoadTypes.Name.name()).toString();

        JsonArray tile = jsonObject.get(FileLoadTypes.Tiles.name()).getAsJsonArray();
        for (JsonElement element: tile) {

            this.tiles.add(element.toString().replace("\"", ""));
        }

    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getTiles() {
        return tiles;
    }

}