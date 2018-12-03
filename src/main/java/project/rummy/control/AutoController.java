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

import java.util.Timer;
import java.util.TimerTask;

public class AutoController extends Controller implements Observer {
  private Strategy strategy;
  private GameState state;
  private boolean activate;
  private boolean hasPlayed;
  private Timer timer;

  public AutoController(Game game, Strategy strategy) {
    game.registerObserver(this);
    this.strategy = strategy;
    this.activate = false;
    this.timer = new Timer();
  }

  @Override
  public String getControllerType() {
    if (strategy instanceof Strategy1) {
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
    this.hasPlayed = false;
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

  private void endTurnIn(int milliseconds) {
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            send(ActionHandler::nextTurn);
          }
        },
        milliseconds
    );
  }

  @Override
  public void update(GameState status) {
    int current = status.getCurrentPlayer();
    if (activate &&
        (!hasPlayed || state.getHandsData()[current].tiles.size() != status.getHandsData()[current].tiles.size())) {
      PlayDirection commands =
          player.status() == PlayerStatus.START ? strategy.iceBreak(status) : strategy.performFullTurn(status);
      send(commands.getCommands(), commands.getChunks());
      hasPlayed = true;
    }
    this.state = status;
  }
}
