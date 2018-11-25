package project.rummy.control;

import project.rummy.entities.HandData;
import project.rummy.entities.TableData;
import project.rummy.entities.TurnStatus;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;

public class NetworkController extends Controller{

  public NetworkController() {
  }

  @Override
  public String getControllerType(){
    return "network";
  }

  @Override
  public void playTurn() {
    Game game = new GameStore(new DefaultGameInitializer()).initializeGame();
    GameState state = game.generateGameState();
    HandData handData = state.getHandsData()[player.getId()];
    TableData tableData = state.getTableData();
    TurnStatus turnStatus = state.getTurnStatus();
    send(handler -> handler.updateFromData(tableData, handData, turnStatus));
  }

  @Override
  public void endTurn() {
  }

  @Override
  public void closeInput() {
    endTurnIn(3000);
  }

  private void endTurnIn(int msec) {
    new java.util.Timer().schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            send(ActionHandler::nextTurn);
          }
        },
        msec
    );
  }
}
