package project.rummy.strategies;

import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class StrategyDrawOnly implements Strategy, Observer {
    private GameState state;

    public StrategyDrawOnly(Game game) {
        game.registerObserver(this);
    }

    @Override
    public PlayDirection iceBreak() {

        List<Command> commands = new ArrayList<>();
        commands.add(handler -> handler.draw());
        return new PlayDirection(commands);
    }

    @Override
    public PlayDirection performFullTurn() {
        List<Command> commands = new ArrayList<>();
        commands.add(handler -> handler.draw());
        return new PlayDirection(commands);
    }

    @Override
    public void update(GameState state) {
        this.state = state;
    }
}
