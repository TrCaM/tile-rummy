package project.rummy.behaviors;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.commands.Command;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayAllMeldsMoveMaker implements ComputerMoveMaker {

    @Override
    public List<Command> calculateMove(GameState state) {

        List<Command> commands = new ArrayList<>();

        List<Tile> handTiles = new ArrayList<>(state.getHandsData()[state.getCurrentPlayer()].tiles);

        List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);

        if(!allMelds.isEmpty()){
            for (Meld m : allMelds) {
                System.out.println(state.getPlayerData()[state.getCurrentPlayer()].name + " plays meld "  + m.tiles().toString());
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
        }
        return commands;
    }
}
