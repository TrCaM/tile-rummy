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
    private ArrayList<Map<Integer, List<String>>> hasMelds;
    private String name;

    public PlayerLoad(Object playerInfoRaw) {
        JSONObject item = (JSONObject)playerInfoRaw;
        JsonElement parser = new JsonParser().parse(item.toJSONString());
        JsonObject jsonObject = parser.getAsJsonObject();
        tiles = new ArrayList<>();
        List<String> meldtiles = new ArrayList<>();
        hasMelds = new ArrayList<>();

        //single items
        this.status = jsonObject.get(FileLoadTypes.Status.name()).toString();
        this.name = jsonObject.get(FileLoadTypes.Name.name()).toString();

        JsonArray tile = jsonObject.get(FileLoadTypes.Tiles.name()).getAsJsonArray();
        for (JsonElement element: tile) {

            this.tiles.add(element.toString().replace("\"", ""));
        }

        JsonArray melds = jsonObject.get(FileLoadTypes.Melds.name()).getAsJsonArray();

        System.out.println(melds.toString());
        JsonObject temp;
        HashMap<Integer, List<String>> tempArr = new HashMap<>();
        for (JsonElement element: melds) {
      //         meldtiles.add(arr.toString().replace("\"", ""));
            if (element.getAsJsonObject().get(FileLoadTypes.meldedTile.name()).toString().equals("[]")) {
                // empty json array
                break;
            }

            for (JsonElement jsonElement: element.getAsJsonObject().get(FileLoadTypes.meldedTile.name()).getAsJsonArray()) {
                meldtiles.add(jsonElement.toString().replace("\"", ""));

            }


            tempArr.put(element.getAsJsonObject().get("Id").getAsInt(), meldtiles);
            this.hasMelds.add(tempArr);
        }

        System.out.println(hasMelds.size());

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

    public ArrayList<Map<Integer, List<String>>> getHasMelds() {
        return hasMelds;
    }
}