package project.rummy.control;

import project.rummy.commands.Command;
import project.rummy.entities.Player;
import project.rummy.entities.PlayerStatus;
import project.rummy.strategies.Strategy;

import java.util.List;

public class AutoController extends Controller {
  private Strategy strategy;

  public AutoController(Strategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public void playTurn() {
    List<Command> commands =
        player.status() == PlayerStatus.START ? strategy.iceBreak() : strategy.performFullTurn();

    commands.forEach(this::send);
  }
}
