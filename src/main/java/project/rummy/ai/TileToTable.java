package project.rummy.ai;

import project.rummy.entities.*;

import java.util.ArrayList;
import java.util.List;

public class TileToTable {


    /**
     * @param direction: left, right, none
     */
    public static int findDetachableMeld(Tile tile, List<Meld> melds, DetachDirection direction){
        int meldId = 0;

        List<Meld> meldFound = new ArrayList<>();
        melds.stream().filter(meld -> meld.tiles().contains(tile)).forEach(meldFound::add);

        if(meldFound.isEmpty()){
            return 0;
        }
        for(Meld m: meldFound){
            int index = m.tiles().indexOf(tile);

            if(direction == DetachDirection.RIGHT
                    && m.type() == MeldType.RUN
                    && index > 2){
                return m.getId();
            }

            if(direction == DetachDirection.LEFT
                    && m.type() == MeldType.RUN
                    && m.tiles().size() - index - 1 >= 3 ){
                return m.getId();
            }

            if(direction == DetachDirection.NONE
                    && m.type() == MeldType.SET
                    && m.tiles().size() == 4 ){
                return m.getId();
            }

        }
        return 0;
    }
}
