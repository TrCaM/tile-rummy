package project.rummy.game.GameReader;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadGameState {
    private ArrayList<PlayerLoad> playerLoad;

    public void read() throws IOException, ParseException {
        this.playerLoad = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader("load\\test.json"));
        JSONObject jsonObject = (JSONObject) object;
        Object name;


        for (PlayerLoadTypes types: PlayerLoadTypes.values()) {
            name = jsonObject.get(types.name());
            playerLoad.add(new PlayerLoad(name));
        }

        System.out.println(playerLoad.get(0).getHasMelds().get(0).values());

    }

    public ArrayList<PlayerLoad> getPlayerLoad() {
        return playerLoad;
    }
}
