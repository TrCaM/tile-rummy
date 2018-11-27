package project.rummy.control;

import io.netty.channel.Channel;
import project.rummy.game.GameState;
import project.rummy.game.GameStatus;
import project.rummy.networks.ClientGameManager;
import project.rummy.observers.Observer;

public class ManualNetworkController extends ManualController implements Observer {

  private Channel channel;
  private boolean isPlaying;

  public ManualNetworkController(Channel channel, ClientGameManager gameManager) {
    super();
    this.channel = channel;
    gameManager.registerObserver(this);
    this.isPlaying = false;
  }

  @Override
  public void update(GameState state) {
    if (state.getCurrentPlayer() == player.getId() && state.getGameStatus() == GameStatus.TURN_END) {
      send(handler -> handler.nextTurn(false));
    }
  }
}
