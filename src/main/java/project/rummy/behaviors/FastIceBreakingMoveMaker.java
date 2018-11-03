package project.rummy.behaviors;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.commands.Command;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;
import java.util.*;

public class FastIceBreakingMoveMaker implements ComputerMoveMaker {

    /***
     * this icebreaking is used for strategy 1 and strategy 2
     */
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
        } else {
            for (Meld m : allMelds) {
                indecies.clear();
                for (int i = 0; i < m.tiles().size(); i++) {
                    indecies.add(i);
                }
                commands.add(handler -> handler.formMeld(indecies.stream().mapToInt(Integer::intValue).toArray()));
                commands.add(handler -> handler.playFromHand(0));
            }
            //TODO send endturn command
            return commands;
        }
    }
}