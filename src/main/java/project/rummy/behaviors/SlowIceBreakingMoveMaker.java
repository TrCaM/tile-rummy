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
    @Override
    public List<Command> calculateMove(GameState state) {

        List<Command> commands = new ArrayList<>();

        List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);

        List<Integer> indecies = new ArrayList<>();


        if (allMelds.isEmpty()) {
            commands.add(handler -> handler.draw());
            return commands;
        } else if (allMelds.stream().mapToInt(Meld::getScore).sum() < 30) {
            commands.add(handler -> handler.draw());
            return commands;
        }else {
            for(int i = 0 ; i < 4; i ++){
                if((state.getPlayerStatuses()[i] == PlayerStatus.START)
                        && (state.getPlayerStatuses()[state.getCurrentPlayer()]) != PlayerStatus.ICE_BROKEN ){
                    for (Meld m : allMelds) {
                        for (int j = 0; j < m.tiles().size(); j++) {
                            indecies.add(j);
                        }
                        commands.add(handler -> handler.formMeld(indecies.stream().mapToInt(Integer::intValue).toArray()));
                    }
                }
                else{
                    commands.add(handler -> handler.draw());
                }
            }
            return  commands;
        }


    }
}
