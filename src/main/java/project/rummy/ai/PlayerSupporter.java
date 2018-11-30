package project.rummy.ai;

import project.rummy.entities.Meld;
import project.rummy.entities.Tile;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerSupporter {
    private List<Tile> handTiles;
    private List<Meld> tableMelds;

    public PlayerSupporter(List<Tile> tiles, List<Meld> melds){
        this.handTiles = tiles;
        this.tableMelds = melds;
    }

    private void clearHints(){
        handTiles.stream().forEach(tile -> tile.setSuggestion(false));
        tableMelds.forEach(meld -> meld.tiles().forEach(tile -> tile.setSuggestion(false)));
    }

    private  boolean suggestFormMeld(){
        clearHints();
        Meld goodMeld = HandMeldSeeker.findNextMelds(handTiles);
        if(goodMeld != null){
            goodMeld.tiles().stream().forEach(tile -> tile.setSuggestion(true));
            return true;
        }
        return false;
    }

    private boolean suggestAddDirectly(){
        clearHints();
        for(Tile tile: handTiles){
            int id = TableMeldSeeker.findDirectMeld(tile.value(), tile.color(), tableMelds);
            if(id != 0){
                tile.setSuggestion(true);
                Meld.getMeldFromId(id, tableMelds).tiles().stream().forEach(tile1 -> tile1.setSuggestion(true));
                return  true;
            }
        }
        return false;
    }


    private boolean suggestManipulationSet(){
        clearHints();
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

    private boolean suggestManipulationRun(){
        clearHints();

        for(Tile tile: handTiles) {
            Map<Meld, Integer> map = CombinationSeeker.formRunBySplitRight(tile.value(), tile.color(),tableMelds);

            if(!map.isEmpty()){
                tile.setSuggestion(true);
                for(Meld m: map.keySet()){
                    for(int i=map.get(m); i<m.tiles().size(); i++){
                        m.tiles().get(i).setSuggestion(true);
                    }
                }
                return true;
            }

            Map<Meld, Integer> map2 = CombinationSeeker.formRunByDetaching(tile.value(), tile.color(),tableMelds);

            if(!map2.isEmpty()){
                tile.setSuggestion(true);
                map2.keySet().stream().forEach(meld -> meld.tiles().get(map2.get(meld)).setSuggestion(true));
                return true;
            }
        }
        return false;

    }

//    public boolean hasHints(){
//        boolean hasHints = showSuggestion();
//        clearHints();
//        return hasHints;
//    }


    public boolean strat1Suggestion(){
        if(suggestFormMeld()){
            return true;
        }
        if(suggestAddDirectly()){
            return true;
        }
        if(suggestManipulationSet()){
            return true;
        }
        if(suggestManipulationRun()){
            return true;
        }
        return false;
    }
}
