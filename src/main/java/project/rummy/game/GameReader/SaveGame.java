package project.rummy.game.GameReader;
import org.json.simple.JSONObject;
import project.rummy.game.GameState;

import java.io.File;
import java.io.IOException;

public class SaveGame {
    //this helps keep code clean

    public void save(GameState state) throws IOException {
        JSONObject gameInfo = new JSONObject();
        WriteGameState writeJson = new WriteGameState();

        gameInfo.put(FileLoadTypes.Deck.name(), state.getFreeTilesCount());
        gameInfo.put(FileLoadTypes.Turn.name(), state.getTurnNumber());
        gameInfo.put(FileLoadTypes.CurrentPlayer.name(), state.getCurrentPlayer());
        gameInfo.put(FileLoadTypes.MeldsOnTable.name(), writeJson.writeMelds(state.getTableData().melds));

        for (int i = 0; i < PlayerLoadTypes.values().length; i++) {
            gameInfo.put(PlayerLoadTypes.values()[i], writeJson.writePlayer(state.getHandsData()[i], state.getPlayerStatuses()[i], state.getPlayerData()[i]));
        }

        gameInfo.put(FileLoadTypes.FreeTiles.name(), writeJson.getFreeTiles(state));
        gameInfo.put(FileLoadTypes.canEnd.name(), state.getTurnStatus().canEnd);
        gameInfo.put(FileLoadTypes.canDraw.name(), state.getTurnStatus().canDraw);
        gameInfo.put(FileLoadTypes.canPlay.name(), state.getTurnStatus().canPlay);
        gameInfo.put(FileLoadTypes.TurnEnd.name(), state.getTurnStatus().isTurnEnd);
        gameInfo.put(FileLoadTypes.IceBroken.name(), state.getTurnStatus().isIceBroken);
        writeJson.writeToJSON(gameInfo);

    }
}
