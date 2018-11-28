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
    public static int findDetachableIdenticalTile (int tileValue, Color tileColor, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        melds.stream().filter(meld -> meld.tiles().size() >= 4).forEach(meldsFound::add);

        for(Meld m: meldsFound){
            for(Tile t: m.tiles()){
                if(t.canFillToRun(tileColor, tileValue)){
                    if(m.type() == MeldType.SET
                            || m.tiles().indexOf(t) == 0
                            || m.tiles().indexOf(t) == m.tiles().size()-1){
                        return m.getId();
                    }
                }
            }

        }
        return 0;
    }


    /**
     * find meld that can split and give:
     * on the left: tiles that can be added to the left of the given tile
     * on the right: a valid meld
     */
    public static int findLeftDetachableTiles (int tileValue, Color tileColor, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        if(tileValue < 1 || tileValue > 11){ return 0;}
        for(Meld m: melds){
            for(Tile t: m.tiles()){
                if(t.canFillToRun(tileColor,tileValue)
                        && m.type() == MeldType.RUN
                        && m.tiles().indexOf(t) > 0
                        && m.tiles().size() - m.tiles().indexOf(t) >= 3){
                    meldsFound.add(m);
                }
            }
        }
        //return the meld with give the max number of detachable tiles
        return meldsFound.isEmpty() ? 0 : Collections.max(meldsFound, Comparator.comparing(meld -> meld.tiles().size())).getId();
    }


    /**
     * find meld that can split and give:
     * on the right: tiles that can be added to the right of the given tile
     * on the left: a valid meld
     */
    public static int findRightDetachableTiles (int tileValue, Color tileColor, List<Meld> melds){
        List<Meld> meldsFound = new ArrayList<>();

        if(tileValue > 12 || tileValue < 3){ return 0;}
        for(Meld m: melds){
            for(Tile t: m.tiles()){
                if(t.canFillToRun(tileColor, tileValue)
                        && m.type() == MeldType.RUN
                        && m.tiles().indexOf(t) != m.tiles().size()-1
                        && m.tiles().indexOf(t) >= 2){
                    meldsFound.add(m);
                }
            }
        }

        //return the meld with give the max number of detachable tiles
        return meldsFound.isEmpty() ? 0 : Collections.max(meldsFound, Comparator.comparing(meld -> meld.tiles().size())).getId();
    }


    /**
     * find meld that the tile can directly add to
     */
    public static int findDirectMeld(int tileValue, Color tileColor, List<Meld> melds){

        //the suitable meld will be the first one appears
        for(Meld m: melds){
            if(m.type() == MeldType.SET && m.tiles().size()==3 && m.getScore()/3==tileValue){
                boolean exist = false;
                for(Tile t: m.tiles()){
                    if(t.color() == tileColor && t.value()==tileValue){ exist=true; }
                }
                if(!exist){ return m.getId(); }
            }else if(m.type()==MeldType.RUN){
                int first = 0;
                if(!m.tiles().get(0).isJoker()){
                    first = m.tiles().get(0).value();
                }else{
                    if(m.tiles().get(1).isJoker()) {
                        first = m.tiles().get(2).value() - 2;
                    }else {
                        first = m.tiles().get(1).value() - 1;
                    }
                }



                int last = first + m.tiles().size() - 1;

                if ((m.tiles().get(0).color() == tileColor || tileColor == Color.ANY)
                        && (tileValue == first - 1 || tileValue == last + 1)) {
                    return m.getId();
                }
            }
        }
        return 0;
    }
}
