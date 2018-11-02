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

        //key is meld and value is tile index
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

    /**
     *
     * @param tileValue
     * @param tileColor
     * @param tableMelds
     * @return
     */
    public static Map<Meld, Integer> formRunByDetaching(int tileValue, Color tileColor, List<Meld> tableMelds) {

        List<Meld> copyMelds = new ArrayList<>(tableMelds);
        //key is meld and value is tile index
        Map<Meld, Integer> map = new HashMap<>();

        int rightValue = tileValue +1;
        while(rightValue <= 13) {
            int meldid = TableMeldSeeker.findDetachableIdenticalTile(rightValue, tileColor, copyMelds);
            if (meldid != 0) {
                Meld m = Meld.getMeldFromId(meldid, copyMelds);
                for (int i = 0; i < m.tiles().size(); i++) {
                    if (m.tiles().get(i).value() == rightValue && m.tiles().get(i).color() == tileColor) {
                        map.put(m, i);
                        copyMelds.remove(copyMelds.indexOf(m));
                    }
                }
                rightValue ++;
            }else{
                break;
            }
        }

        int leftValue = tileValue - 1;
        while(leftValue >= 1) {
            int meldid = TableMeldSeeker.findDetachableIdenticalTile(leftValue, tileColor, copyMelds);
            if (meldid != 0) {
                Meld m = Meld.getMeldFromId(meldid, copyMelds);
                for (int i = 0; i < m.tiles().size(); i++) {
                    if (m.tiles().get(i).value() == leftValue && m.tiles().get(i).color() == tileColor) {
                        map.put(m, i);
                        copyMelds.remove(copyMelds.indexOf(m));
                    }
                }
                leftValue --;
            }else{
                break;
            }
        }

        return map;
    }

    /**
     *
     * @param tileValue
     * @param tileColor
     * @param tableMelds
     */
    public static void formRunGeneral(int tileValue, Color tileColor, List<Meld> tableMelds){

        int rightId = TableMeldSeeker.findRightDetachableTiles(tileValue, tileColor, tableMelds);
        Meld rightMeld = rightId == 0 ? null : Meld.getMeldFromId(rightId, tableMelds);

        if(rightMeld != null){
            for(int i=0; i<rightMeld.tiles().size(); i++){
                if(rightMeld.tiles().get(i).value()== tileValue && rightMeld.tiles().get(i).color() == tileColor){
                    //this is the number of tiles that we can detach
                    if(rightMeld.tiles().size() - i - 1 >= 2){
                        //TODO return something
                    }
                }
            }
        }

        int leftId = TableMeldSeeker.findRightDetachableTiles(tileValue, tileColor, tableMelds);
        Meld leftMeld = leftId == 0 ? null : Meld.getMeldFromId(leftId, tableMelds);

        if(leftMeld != null){
            for(int i=0; i<leftMeld.tiles().size(); i++){
                if(leftMeld.tiles().get(i).value()== tileValue && leftMeld.tiles().get(i).color() == tileColor){
                    //this is the number of tiles that we can detach
                    if(i >= 2){
                        //TODO return something
                    }
                }
            }
        }


    }
}
