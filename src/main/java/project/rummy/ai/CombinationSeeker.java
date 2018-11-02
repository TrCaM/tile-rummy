package project.rummy.ai;

import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinationSeeker {

    /**
     * find all possible melds from which tiles can be detached
     * and form a new set with a given tile
     * @param tileValue: value of the tile forming set with
     * @param tileColor: color of the tile forming set with
     * @param tableMelds: all melds on the table
     * @return: map where key is a suitable meld, and value is the index of the needed tile
     */

    public static Map<Meld, Integer> formSet(int tileValue, Color tileColor, List<Meld> tableMelds){
        List<Meld> copyMelds = new ArrayList<>(tableMelds);

        //key is meld id and value is tile index
        Map<Meld, Integer> map = new HashMap<>();

        for(Color c: Color.values()){
            if(c != tileColor){
                int meldid = TableMeldSeeker.findDetachableIdenticalTile(tileValue, c, copyMelds);
                if(meldid != 0){
                    Meld m = Meld.getMeldFromId(meldid, copyMelds);
                    for(int i=0; i<m.tiles().size(); i++){
                        if(m.tiles().get(i).value()==tileValue && m.tiles().get(i).color()==c){
                            map.put(m, i);
                            copyMelds.remove(copyMelds.indexOf(m));
                        }
                    }
                }
            }
        }
        return map;
    }

    public static void formRun(int tileValue, Color tileColor, List<Meld> tableMelds){

    }
}
