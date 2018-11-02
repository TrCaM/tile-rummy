package project.rummy.ai;

import project.rummy.entities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableMeldSeeker {


    /**
     * find meld that can be detached to give a identical tile
     */
    public static Meld findDetachableIdenticalTile (Tile tile, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        melds.stream().filter(meld -> meld.tiles().size() >= 4).forEach(meldsFound::add);

        for(Meld m: meldsFound){
            for(Tile t: m.tiles()){
                if(t.value()== tile.value() && t.color()==tile.color()){
                    if(m.type() == MeldType.SET
                            || m.tiles().indexOf(tile) == 0
                            || m.tiles().indexOf(tile) == m.tiles().size()-1){
                        return m;
                    }
                }
            }

        }
        return null;
    }


    /**
     * find meld that can split and give:
     * on the left: tiles that can be added to the left of the given tile
     * on the right: a valid meld
     */
    public static Meld findLeftDetachableTiles (Tile tile, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();
        if(tile.value() >= 2 && tile.value()<= 11) {
            melds.stream()
                    .filter(meld -> meld.tiles().contains(tile)
                            && meld.type() == MeldType.RUN
                            && meld.tiles().indexOf(tile)>0
                            && meld.tiles().size() - meld.tiles().indexOf(tile) >= 3)
                    .forEach(meldsFound::add);
        }
        //return the meld with give the max number of detachable tiles
        return meldsFound.isEmpty() ? null : Collections.max(meldsFound, Comparator.comparing(meld -> meld.tiles().size()));
    }


    /**
     * find meld that can split and give:
     * on the right: tiles that can be added to the right of the given tile
     * on the left: a valid meld
     */
    public static Meld findRightDetachableTiles (Tile tile, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();
        if(tile.value() >= 3 && tile.value() <= 12) {
            melds.stream()
                    .filter(meld -> meld.tiles().contains(tile)
                            && meld.type() == MeldType.RUN
                            && meld.tiles().indexOf(tile) != meld.tiles().size()-1
                            && meld.tiles().indexOf(tile) >= 2)
                    .forEach(meldsFound::add);
        }

        //return the meld with give the max number of detachable tiles
        return meldsFound.isEmpty() ? null : Collections.max(meldsFound, Comparator.comparing(meld -> meld.tiles().size()));
    }


    /**
     * find meld that the tile can directly add to
     */
    public static Meld findDirectMeld(Tile tile, List<Meld> melds){

        //the suitable meld will be the first one appears
        for(Meld m: melds){
            if(m.type() == MeldType.SET && m.tiles().get(0).value() == tile.value() && !m.tiles().contains(tile)){
                return m;
            }

            int first = m.tiles().get(0).value();
            int last  = first + m.tiles().size() - 1;
            if(m.tiles().get(0).color() == tile.color()
                    && (tile.value() == first -1 || tile.value() == last +1)){
                return m;
            }
        }
        return null;
    }
}
