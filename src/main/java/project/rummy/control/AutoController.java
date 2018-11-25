package project.rummy.control;

import project.rummy.commands.PlayDirection;
import project.rummy.entities.PlayerStatus;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;
import project.rummy.strategies.Strategy;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;

public class AutoController extends Controller implements Observer {
  private Strategy strategy;
  private GameState state;
  private boolean activate;

  public AutoController(Game game, Strategy strategy) {
    game.registerObserver(this);
    this.strategy = strategy;
    this.activate = false;
  }

  @Override
  public String getControllerType(){
    if(strategy instanceof Strategy1){
      return "strategy1";
    }
    if (strategy instanceof Strategy2) {
      return "strategy2";
    }
    if (strategy instanceof Strategy3) {
      return "strategy3";
    }

    return "strategy4";
  }

  @Override
  public void playTurn() {
    this.activate = true;
  }

  @Override
  public void endTurn() {
    this.activate = false;
  }

  @Override
  public void closeInput() {
    this.activate = false;
    endTurnIn(3000);
  }

  /**
   * End this turn after a fixed duration
   * @param msec the duration in milliseconds
   */
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

  @Override
  public void update(GameState status) {
    this.state = status;
    if (activate) {
      PlayDirection commands =
          player.status() == PlayerStatus.START ? strategy.iceBreak(state) : strategy.performFullTurn(state);
      send(commands.getCommands(), commands.getChunks());
    }
  }
}
