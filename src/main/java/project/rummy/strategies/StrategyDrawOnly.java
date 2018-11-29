package project.rummy.strategies;

import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class StrategyDrawOnly implements Strategy {

    @Override
    public PlayDirection iceBreak(GameState gameState) {
        List<Command> commands = new ArrayList<>();
        commands.add(handler -> {
            handler.drawAndEndTurn();
            handler.endTurn();
        });
        return new PlayDirection(commands);
    }

    @Override
    public PlayDirection performFullTurn(GameState gameState) {
        List<Command> commands = new ArrayList<>();
        commands.add(handler -> {
            handler.drawAndEndTurn();
            handler.endTurn();
        });
        return new PlayDirection(commands);
    }
}
