package project.rummy.behaviors;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.commands.Command;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class PlayMeldMoveMaker implements ComputerMoveMaker {

    @Override
    public List<Command> calculateMove(GameState state) {

        List<Command> commands = new ArrayList<>();

        List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;

        Meld nextMeld = HandMeldSeeker.findNextMelds(handTiles);

        if(nextMeld != null){
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < nextMeld.tiles().size(); i++) {
                indexes.add(i);
            }
            commands.add(handler -> handler.formMeld(indexes.stream().mapToInt(Integer::intValue).toArray()));
            commands.add(handler -> handler.playFromHand(0));
        }
        return commands;
    }
}
