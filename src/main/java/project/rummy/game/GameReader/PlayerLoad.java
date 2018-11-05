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
        ArrayList<Meld> test = new ArrayList<Meld>();
        // write the melds to be played or make it stay empity
        data[0] = new HandData(new Hand(tiles, test));



        parser = new JsonParser().parse(object.get("Player 1").toString());
        jsonObject = parser.getAsJsonObject();

        tiles = new ArrayList<>();
        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }

        data[1] = new HandData(new Hand(tiles, new ArrayList<Meld>()));

        parser = new JsonParser().parse(object.get("Player 2").toString());
        jsonObject = parser.getAsJsonObject();
        tiles = new ArrayList<>();

        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            tiles.add(tile);
        }
        data[2] = new HandData(new Hand(tiles, new ArrayList<Meld>()));

        parser = new JsonParser().parse(object.get("Player 3").toString());
        jsonObject = parser.getAsJsonObject();
        tiles = new ArrayList<>();

        for (JsonElement element: jsonObject.get("Tiles").getAsJsonArray()) {
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

    public  PlayerData [] getPlayerDatas(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get("HUMAN").toString());
        JsonObject jsonObject = parser.getAsJsonObject();
        playerData = new PlayerData[4];

        playerData[0] = new PlayerData(jsonObject.get(FileLoadTypes.Name.name()).getAsString(), jsonObject.get(FileLoadTypes.Controller.name()).getAsString());

        parser = new JsonParser().parse(object.get("Player 1").toString());
        jsonObject = parser.getAsJsonObject();

        playerData[1] = new PlayerData(jsonObject.get(FileLoadTypes.Name.name()).getAsString(), jsonObject.get(FileLoadTypes.Controller.name()).getAsString());
        parser = new JsonParser().parse(object.get("Player 2").toString());
        jsonObject = parser.getAsJsonObject();

        playerData[2] = new PlayerData(jsonObject.get(FileLoadTypes.Name.name()).getAsString(), jsonObject.get(FileLoadTypes.Controller.name()).getAsString());
        parser = new JsonParser().parse(object.get("Player 3").toString());
        jsonObject = parser.getAsJsonObject();
        playerData[3] = new PlayerData(jsonObject.get(FileLoadTypes.Name.name()).getAsString(), jsonObject.get(FileLoadTypes.Controller.name()).getAsString());

        return this.playerData;
    }

    public TableData getTableData(JSONObject object) {
        JsonElement parser = new JsonParser().parse(object.get(FileLoadTypes.FreeTiles.name()).toString());
        JsonArray jsonArray = parser.getAsJsonArray();
        TableData data = new TableData();
        ArrayList<Tile> tiles = new ArrayList<>();
        Color color;
        int tile_num;
        Tile tile;
        Meld meld;


        for (JsonElement element : jsonArray) {
            color = getColor(element.getAsString().charAt(0));
            tile_num = Integer.parseInt(element.getAsString().substring(1));
            tile = new Tile(color, tile_num);
            data.freeTiles.add(tile);
        }

        // add code to load from meld
        parser = new JsonParser().parse(object.get(FileLoadTypes.MeldsOnTable.name()).toString());
        jsonArray = parser.getAsJsonArray();

        for (JsonElement element : jsonArray) {
            tiles = new ArrayList<>();
            for (JsonElement element1 : element.getAsJsonObject().get(FileLoadTypes.Meld.name()).getAsJsonArray()) {
                color = getColor(element1.getAsString().charAt(0));
                tile_num = Integer.parseInt(element1.getAsString().substring(1));
                tile = new Tile(color, tile_num);
                tiles.add(tile);


            }
            // fix this later
            meld = Meld.createMeld(tiles);

            data.melds.add(meld);
        }


        return data;
    }

    public TurnStatus getTurnStatuses(JSONObject jsonObject) {
        JsonElement parser = new JsonParser().parse(jsonObject.toJSONString());
        TurnStatus status = new TurnStatus();

        status.canDraw = parser.getAsJsonObject().get(FileLoadTypes.canDraw.name()).getAsBoolean();
        status.canEnd = parser.getAsJsonObject().get(FileLoadTypes.canEnd.name()).getAsBoolean();
        status.canPlay = parser.getAsJsonObject().get(FileLoadTypes.canPlay.name()).getAsBoolean();
        status.isTurnEnd = parser.getAsJsonObject().get(FileLoadTypes.TurnEnd.name()).getAsBoolean();
        status.isIceBroken = parser.getAsJsonObject().get(FileLoadTypes.IceBroken.name()).getAsBoolean();

        return status;
    }

    public  void getMeldOnTable() {}



}