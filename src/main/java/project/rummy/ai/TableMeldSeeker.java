package project.rummy.ai;

import project.rummy.entities.*;

import java.util.ArrayList;
import java.util.List;

public class TableMeldSeeker {


    /**
     * @param direction: left, right, none
     */
    public static int findDetachableMeld(Tile tile, List<Meld> melds, DetachDirection direction){
        List<Meld> meldFound = new ArrayList<>();
        melds.stream().filter(meld -> meld.tiles().contains(tile)).forEach(meldFound::add);

        if(meldFound.isEmpty()){
            return 0;
        }
        for(Meld m: meldFound){
            int index = m.tiles().indexOf(tile);

            if(direction == DetachDirection.RIGHT && m.type() == MeldType.RUN && index > 2){
                return m.getId();
            }
            if(direction == DetachDirection.LEFT && m.type() == MeldType.RUN && m.tiles().size() - index - 1 >= 3 ){
                return m.getId();
            }
            if(direction == DetachDirection.NONE && m.type() == MeldType.SET && m.tiles().size() == 4 ){
                return m.getId();
            }
        }
        return 0;
    }

    /**
     * find meld that the tile can directly add to
     */
    public static int findDirectMeldToAdd(Tile tile, List<Meld> melds){

        //the suitable meld will be the first one appears
        for(Meld m: melds){
            if(m.type() == MeldType.SET && m.tiles().get(0).value() == tile.value() && !m.tiles().contains(tile)){
                return m.getId();
            }
            int first = m.tiles().get(0).value();
            int last  = first + m.tiles().size() - 1;

            if(m.tiles().get(0).color() == tile.color()
                    && (tile.value() == first -1 || tile.value() == last +1)){
                return m.getId();
            }
        }
        return 0;
    }
}
