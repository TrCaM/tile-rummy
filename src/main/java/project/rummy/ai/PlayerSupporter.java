package project.rummy.ai;

import project.rummy.entities.Meld;
import project.rummy.entities.Tile;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerSupporter {

    public boolean suggestFormMeld(List<Tile> handTiles){
        Meld goodMeld = HandMeldSeeker.findNextMelds(handTiles);
        if(goodMeld != null){
            goodMeld.tiles().stream().forEach(tile -> tile.setSuggestion(true));
            return true;
        }
        return false;
    }


    public boolean suggestManipulationSet(List<Tile> handTiles, List<Meld> tableMelds){

        for(Tile tile: handTiles) {
            List<Tile> goodTiles = new ArrayList<>();
            Map<Meld, Integer> map;
            goodTiles.add(tile);

            for (Tile t : handTiles) {
                if (t.value() == tile.value() && t.color() != tile.color()) {
                    goodTiles.add(t);
                    break;
                }
            }
            map = CombinationSeeker.formSet(goodTiles,tableMelds);
            if(goodTiles.size() + map.size() >= 3){
                goodTiles.stream().forEach(tile1 -> tile1.setSuggestion(true));
                map.keySet().stream().forEach(meld -> meld.tiles().get(map.get(meld)).setSuggestion(true));
                return true;
            }
        }
        return false;
    }

    public boolean suggestManipulationRun(List<Tile> handTiles, List<Meld> tableMelds){

        for(Tile tile: handTiles) {
            Map<Meld, Integer> map = CombinationSeeker.formRunBySplitRight(tile.value(), tile.color(),tableMelds);

            if(map.isEmpty()){
                map = CombinationSeeker.formRunByDetaching(tile.value(), tile.color(),tableMelds);
            }

            if(!map.isEmpty()){
                tile.setSuggestion(true);
                for(Meld m: map.keySet()){
                    m.tiles().get(map.get(m)).setSuggestion(true);
                }
                return true;
            }
        }
        return false;

    }


    public void showSuggestion(List<Tile> handTiles, List<Meld> tableMeld){

        if(suggestFormMeld(handTiles)){
            return;
        }
        if(suggestManipulationSet(handTiles,tableMeld)){
            return;
        }
        suggestManipulationRun(handTiles, tableMeld);
    }
}
