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
            for (Meld m : allMelds) {
                List<Integer> indecies = new ArrayList<>();
                for (int i = 0; i < m.tiles().size(); i++) {
                    indecies.add(handTiles.indexOf(m.tiles().get(i)));
                }
                commands.add(handler -> {
                    handler.formMeld(indecies.stream().mapToInt(Integer::intValue).toArray());
                    handler.playFromHand(0);
                    handler.submit();
                });
                Collections.sort(indecies);
                for(int k=indecies.size()-1; k>=0; k--) {
                    handTiles.remove(handTiles.get(indecies.get(k)));
                }
            }
            return commands;
        }
    }
}