package project.rummy.strategies;

import project.rummy.behaviors.*;
import project.rummy.commands.Command;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;


/**
 * Aggressive Strategy (or strategy 1 in the specification). Specifically, computer controllers using
 * this strategy why try to play anything that he has in his hand and try to manipulate the table
 * to play as much as possible
 */
public class Strategy1 implements Strategy, Observer {
  private GameState state;

  public Strategy1(Game game) {
    game.registerObserver(this);
  }

  @Override
  public List<Command> iceBreak() {
    ComputerMoveMaker move = new FastIceBreakingMoveMaker();
    return move.calculateMove(state);
  }

  @Override
  public List<Command> performFullTurn() {
    List<Command> commands = new ArrayList<>();
    List<Command> recievedCmd;
    boolean mustDraw = true;

    ComputerMoveMaker playMeld = new PlayMeldMoveMaker();

    recievedCmd = playMeld.calculateMove(state);
    while(!recievedCmd.isEmpty()){
        mustDraw = false;
        commands.addAll(recievedCmd);
        recievedCmd = playMeld.calculateMove(state);
    }

    ComputerMoveMaker playTile = new FastTilesOnlyMoveMaker();

    recievedCmd = playTile.calculateMove(state);
    while(!recievedCmd.isEmpty()){
        mustDraw = false;
        commands.addAll(recievedCmd);
        recievedCmd = playTile.calculateMove(state);
    }

    if(mustDraw){ commands.add(handler -> handler.draw()); }

    return commands;
  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }
}
