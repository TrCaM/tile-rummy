package project.rummy.game.GameReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import project.rummy.entities.*;
import project.rummy.game.GameState;

import javax.xml.bind.Element;
import java.util.*;

public class PlayerLoad {
    private HandData data[];
    private PlayerStatus status[];
    private PlayerData playerData[];
    private TableData tableData;

    public HandData[] configHandData(JSONObject object)
    {
        List<Tile> tiles = new ArrayList<>();
        Color color;
        int tile_num;
        Tile tile;
        Hand hand;

        data = new HandData[4];


        JsonElement parser = new JsonParser().parse(object.get("HUMAN").toString());
        JsonObject jsonObject = parser.getAsJsonObject();


        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }
        data[0] = new HandData(new Hand(tiles, new ArrayList<Meld>()));



        parser = new JsonParser().parse(object.get("Player 1").toString());
        jsonObject = parser.getAsJsonObject();

        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            tiles = new ArrayList<>();
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }

        data[1] = new HandData(new Hand(tiles, new ArrayList<Meld>()));

        parser = new JsonParser().parse(object.get("Player 2").toString());
        jsonObject = parser.getAsJsonObject();

        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            tiles = new ArrayList<>();
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }
        data[2] = new HandData(new Hand(tiles, new ArrayList<Meld>()));

        parser = new JsonParser().parse(object.get("Player 3").toString());
        jsonObject = parser.getAsJsonObject();

        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            tiles = new ArrayList<>();
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }
        data[3] = new HandData(new Hand(tiles, new ArrayList<Meld>()));


        return data;
    }

    private Color getColor(char c) {
        if (c == 'R') {
           return Color.RED;
        }
        if (c == 'B') {
            return Color.BLACK;
        }
        if (c == 'G') {
            return Color.GREEN;
        }
        if (c == 'O') {
            return Color.ORANGE;
        }


        // color is not detected
        return null;
    }

    public PlayerStatus[] getStatuses(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get("HUMAN").toString());
        JsonObject jsonObject = parser.getAsJsonObject();
        PlayerStatus status[] = new PlayerStatus[4];
        status[0] = check(jsonObject.get(FileLoadTypes.Status.name()).getAsString());

        parser = new JsonParser().parse(object.get("Player 1").toString());
        jsonObject = parser.getAsJsonObject();
        status[1] = check(jsonObject.get(FileLoadTypes.Status.name()).getAsString());

        parser = new JsonParser().parse(object.get("Player 2").toString());
        jsonObject = parser.getAsJsonObject();
        status[2] = check(jsonObject.get(FileLoadTypes.Status.name()).getAsString());

        parser = new JsonParser().parse(object.get("Player 3").toString());
        jsonObject = parser.getAsJsonObject();
        status[3] = check(jsonObject.get(FileLoadTypes.Status.name()).getAsString());

        return status;
    }

    private PlayerStatus check(String str) {
        if (str.toUpperCase().equals("NOT PLAYED")) {
            return PlayerStatus.START;
        }
        else if ((str.toUpperCase().equals("ICE BROKEN"))) {
            return PlayerStatus.ICE_BROKEN;
        }
        return null;
    }

    public  PlayerData getPlayerDatas(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get("HUMAN").toString());
        JsonObject jsonObject = parser.getAsJsonObject();
        playerData = new PlayerData[4];



        return null;
    }

    public TableData getTableData(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get(FileLoadTypes.MeldsOnTable.name()).toString());
        JsonArray jsonArray = parser.getAsJsonArray();
        TableData data = new TableData();
        Color color;
        int tile_num;
        Tile tile;
        Meld meld;

        for (JsonElement element : jsonArray){
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            meld = new Meld(tile);
            data.melds.add(meld);
        }


        return data;
    }

    public  void getMeldOnTable() {}



}