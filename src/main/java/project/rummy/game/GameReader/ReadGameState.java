package project.rummy.game.GameReader;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import project.rummy.entities.TableData;
import project.rummy.game.Game;
import project.rummy.game.GameState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadGameState {

    public GameState read() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader("load\\test.json"));
        JSONObject jsonObject = (JSONObject) object;
        JsonElement parse = new JsonParser().parse(jsonObject.toJSONString());
        JsonObject simpJson = parse.getAsJsonObject();

        GameState state = new GameState();

        PlayerLoad load = new PlayerLoad();
        state.setHandsData(load.configHandData(jsonObject));
        state.setTurnNumber(simpJson.get(FileLoadTypes.Turn.name()).getAsInt());
        state.setCurrentPlayer(simpJson.get(FileLoadTypes.CurrentPlayer.name()).getAsInt());
        state.setFreeTilesCount(simpJson.get(FileLoadTypes.Deck.name()).getAsInt());
        state.setStatuses(load.getStatuses(jsonObject));

        state.setTableData(new TableData());
        return state;

    }

}
