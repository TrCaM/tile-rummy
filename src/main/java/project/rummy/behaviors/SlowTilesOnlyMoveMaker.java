package project.rummy.behaviors;

import project.rummy.commands.Command;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class SlowTilesOnlyMoveMaker implements ComputerMoveMaker{
    @Override
    public List<Command> calculateMove(GameState state) {
        List<Command> commands = new ArrayList<>();
        List<Command> receivedCommands;

        ComputerMoveMaker move = new PlaySingleTileMoveMaker();
        receivedCommands = move.calculateMove(state);
        if(receivedCommands.isEmpty()){
            return commands;
        }

        for(int i=0; i<4 && i!= state.getCurrentPlayer(); i++) {
            while(state.getHandsData()[state.getCurrentPlayer()].tiles.size() - state.getHandsData()[i].tiles.size() >= 3){
                if(!receivedCommands.isEmpty()){
                    commands.addAll(receivedCommands);
                    receivedCommands = move.calculateMove(state);
                }
            }
        }
        return commands;
    }

}
