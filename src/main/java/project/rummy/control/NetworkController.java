package project.rummy.control;

import io.netty.channel.Channel;
import project.rummy.entities.HandData;
import project.rummy.entities.TableData;
import project.rummy.entities.TurnStatus;
import project.rummy.game.*;
import project.rummy.networks.ClientGameManager;
import project.rummy.observers.Observer;

public class NetworkController extends Controller implements Observer {
  private int playerId;
  private Channel channel;
  private boolean isPlaying;

  public NetworkController(Channel channel, ClientGameManager gameManager, int playerId) {
    this.channel = channel;
    gameManager.registerObserver(this);
    this.isPlaying = false;
    this.playerId = playerId;
  }

  @Override
  public String getControllerType() {
    return "network";
  }

  @Override
  public void playTurn() {
    isPlaying = true;
  }

  @Override
  public void endTurn() {
    isPlaying = false;
  }

  @Override
  public void closeInput() {
  }

  public void update(GameState state) {
    if (isPlaying) {
      if (state.getGameStatus() == GameStatus.TURN_END && state.getCurrentPlayer() == playerId) {
        send(handler -> handler.nextTurn(false));
      } else if (state.getCurrentPlayer() == player.getId()){
        HandData handData = state.getHandsData()[player.getId()];
        TableData tableData = state.getTableData();
        TurnStatus turnStatus = state.getTurnStatus();
        send(handler -> handler.updateFromData(tableData, handData, turnStatus));
      }
    }
  }
}
