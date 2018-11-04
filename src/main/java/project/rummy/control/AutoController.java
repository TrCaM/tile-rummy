package project.rummy.control;

import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.entities.Player;
import project.rummy.entities.PlayerStatus;
import project.rummy.strategies.Strategy;
import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;

import java.util.List;

public class AutoController extends Controller {
  private Strategy strategy;

  public AutoController(Strategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String getControllerType(){
    if(strategy instanceof Strategy1){
      return "strategy1";
    }
    if (strategy instanceof Strategy2) {
      return "strategy2";
    }

    return "strategy3";
  }

  @Override
  public void playTurn() {
    PlayDirection commands =
        player.status() == PlayerStatus.START ? strategy.iceBreak() : strategy.performFullTurn();

    send(commands.getCommands(), commands.getChunks());
  }
}
