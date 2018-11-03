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

        List<Tile> handTiles = new ArrayList<>(state.getHandsData()[state.getCurrentPlayer()].tiles);

        List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);

        if (allMelds.isEmpty()) {
            commands.add(handler -> handler.draw());
            return commands;
        } else if (allMelds.stream().mapToInt(Meld::getScore).sum() < 30) {
            commands.add(handler -> handler.draw());
            return commands;
        } else {
            ComputerMoveMaker move = new PlayAllMeldsMoveMaker();
            return move.calculateMove(state);
        }
    }
}