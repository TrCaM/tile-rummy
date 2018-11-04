package project.rummy.behaviors;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.commands.Command;
import project.rummy.entities.Meld;
import project.rummy.entities.PlayerStatus;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class SlowIceBreakingMoveMaker implements ComputerMoveMaker {

    /**
     * this type of icebreaking is used for strategy 2
     */
    @Override
    public List<Command> calculateMove(GameState state) {

        List<Command> commands = new ArrayList<>();

        List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);


        if (allMelds.isEmpty()) {
            commands.add(handler -> handler.draw());
            return commands;
        } else if (allMelds.stream().mapToInt(Meld::getScore).sum() < 30) {
            commands.add(handler -> handler.draw());
            return commands;
        }else {
            for(int i = 0 ; i < 4 ; i ++){
                if((state.getPlayerStatuses()[i] == PlayerStatus.ICE_BROKEN && i!= state.getCurrentPlayer())){
                    ComputerMoveMaker move = new FastIceBreakingMoveMaker();
                    return move.calculateMove(state);
                }
            }
            commands.add(handler -> handler.draw());
            return  commands;
        }
    }
}
