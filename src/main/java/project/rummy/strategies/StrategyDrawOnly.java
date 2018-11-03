package project.rummy.strategies;

import project.rummy.commands.Command;
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
    public List<Command> iceBreak() {

        List<Command> commands = new ArrayList<>();
        commands.add(handler -> handler.draw());
        return commands;
    }

    @Override
    public List<Command> performFullTurn() {
        List<Command> commands = new ArrayList<>();
        commands.add(handler -> handler.draw());
        return commands;
    }

    @Override
    public void update(GameState state) {
        this.state = state;
    }
}
