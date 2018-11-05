package project.rummy.game.GameReader;


import com.almasb.fxgl.entity.component.Component;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import project.rummy.game.GameState;

import java.io.FileReader;
import java.io.IOException;

public class ReadGameState extends Component {

    public GameState read() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.parse(new FileReader("load\\TestCase14a.json"));
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
        state.setPlayerData(load.getPlayerDatas(jsonObject));
        state.setTableData(load.getTableData(jsonObject));

        state.setTurnStatus(load.getTurnStatuses(jsonObject));

        return state;

    }

}
