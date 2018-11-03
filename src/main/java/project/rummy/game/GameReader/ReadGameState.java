package project.rummy.game.GameReader;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadGameState {
    private ArrayList<PlayerLoad> playerLoad;
    private int turn;
    private String currentPlayer;
    private String TileDrawn;
    private int deck;
    private List<Map<Integer, List<String>>> hasMelds;



    public void read() throws IOException, ParseException {
        this.playerLoad = new ArrayList<>();
        hasMelds = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        // change this to a different thing
        Object object = jsonParser.parse(new FileReader("load\\test.json"));
        JSONObject jsonObject = (JSONObject) object;
        Object name;


        for (PlayerLoadTypes types: PlayerLoadTypes.values()) {
            name = jsonObject.get(types.name());
            playerLoad.add(new PlayerLoad(name));
        }

        JsonElement parser = new JsonParser().parse(jsonObject.toJSONString());
        JsonObject simJSON = parser.getAsJsonObject();
        this.deck = simJSON.get(FileLoadTypes.Deck.name()).getAsInt();
        this.turn = simJSON.get(FileLoadTypes.Turn.name()).getAsInt();
        this.currentPlayer = simJSON.get(FileLoadTypes.CurrentPlayer.name()).getAsString().replace("\"", "");
        this.TileDrawn = simJSON.get(FileLoadTypes.TileDrawn.name()).getAsString().replace("\"", "");

        JsonArray object1 = simJSON.get(FileLoadTypes.MeldsOnTable.name()).getAsJsonArray();
        JsonArray array;
        Map<Integer, List<String>> temp;
        List<String> meldtiles;


        for (JsonElement element: object1) {
            temp = new HashMap<>();
            meldtiles = new ArrayList<>();
            array = element.getAsJsonObject().get(FileLoadTypes.Meld.name()).getAsJsonArray();
            if (element.getAsJsonObject().get(FileLoadTypes.Meld.name()).toString().equals("[]") || element.getAsJsonObject().get(FileLoadTypes.Meld.name()).toString() == null) {
                System.out.println("Something went wrong");
                break;
            }

            for (JsonElement meldType: array) {
                meldtiles.add(meldType.getAsString());
            }
            temp.put(element.getAsJsonObject().get(FileLoadTypes.Id.name()).getAsInt(), meldtiles);
            this.hasMelds.add(temp);
        }

    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDeck() {
        return deck;
    }

    public int getTurn() {
        return turn;
    }

    public String getTileDrawn() {
        return TileDrawn;
    }

    public ArrayList<PlayerLoad> getPlayerLoad() {
        return playerLoad;
    }

    public List<Map<Integer, List<String>>> getHasTableMelds() {
        return hasMelds;
    }
}
