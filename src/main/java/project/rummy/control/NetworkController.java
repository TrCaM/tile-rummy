package project.rummy.control;

import project.rummy.game.*;
import project.rummy.networks.ClientGameManager;
import project.rummy.observers.Observer;

public class NetworkController extends Controller implements Observer {
  private int playerId;
  private boolean isPlaying;

  public NetworkController(ClientGameManager gameManager, int playerId) {
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
        send(handler -> handler.updateFromData(state, playerId));
      }
    }
  }
}
