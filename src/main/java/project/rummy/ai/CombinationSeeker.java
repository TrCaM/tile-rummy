package project.rummy.ai;

import project.rummy.entities.Color;
import project.rummy.entities.Meld;
import project.rummy.entities.MeldType;
import project.rummy.entities.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinationSeeker {

    /**
     * find all possible melds from which tiles can be detached
     * and form a new set with a given tile
     * @param tableMelds: all melds on the table
     * @return: map where key is a suitable meld, and value is the index of the needed tile
     */

    public static Map<Meld, Integer> formSet(List<Tile> tiles, List<Meld> tableMelds){
        List<Meld> copyMelds = new ArrayList<>(tableMelds);

        //key is meld and value is tile index
        Map<Meld, Integer> map = new HashMap<>();

        int tileValue = tiles.get(0).value();
        for(Color c: Color.values()){
            if(c != Color.ANY) {
                boolean color2 = true;
                if (tiles.size() > 1) {
                    color2 = c != tiles.get(1).color();
                }
                if (c != tiles.get(0).color() && color2) {
                    int meldid = TableMeldSeeker.findDetachableIdenticalTile(tiles.get(0).value(), c, copyMelds);
                    if (meldid != 0) {
                        Meld m = Meld.getMeldFromId(meldid, copyMelds);
                        for (int i = 0; i < m.tiles().size(); i++) {
                            if(m.type() == MeldType.RUN){
                                if (m.tiles().get(i).canFillToRun(c, tileValue)
                                        && (i==0 || i == m.tiles().size()-1)) {
                                    map.put(m, i);
                                    copyMelds.remove(m);
                                }
                            } else if (m.tiles().get(i).isJoker()
                                || m.tiles().get(i).canFillToRun(c, tiles.get(0).value())) {
                                map.put(m, i);
                                copyMelds.remove(m);
                            }
                        }
                    }
                }
            }
        }
//        for(Meld m: map.keySet()){
//            System.out.println(m.tiles().toString() + "    " + m.tiles().get(map.get(m)));
//        }
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
                    if (m.tiles().get(i).canFillToRun(tileColor,rightValue)) {
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
                    if (m.tiles().get(i).canFillToRun(tileColor, leftValue)) {
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
    public static Map<Meld, Integer> formRunBySplitRight(int tileValue, Color tileColor, List<Meld> tableMelds) {

        Map<Meld, Integer> map = new HashMap<>();
        int rightId = TableMeldSeeker.findRightDetachableTiles(tileValue, tileColor, tableMelds);
        Meld rightMeld = rightId == 0 ? null : Meld.getMeldFromId(rightId, tableMelds);

        if (rightMeld != null) {
            for (int i = 0; i < rightMeld.tiles().size(); i++) {
                if (rightMeld.tiles().get(i).value() == tileValue && rightMeld.tiles().get(i).color() == tileColor) {
                    //this is the number of tiles that we can detach
                    if (rightMeld.tiles().size() - i - 1 >= 2) {
                        map.put(rightMeld, i);
                        return map;
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
     */
    public static Map<Meld, Integer> formRunBySplitLeft(int tileValue, Color tileColor, List<Meld> tableMelds){

        Map<Meld, Integer> map = new HashMap<>();

        int leftId = TableMeldSeeker.findLeftDetachableTiles(tileValue, tileColor, tableMelds);
        Meld leftMeld = leftId == 0 ? null : Meld.getMeldFromId(leftId, tableMelds);

        if(leftMeld != null){
            for(int i=0; i<leftMeld.tiles().size(); i++){
                if(leftMeld.tiles().get(i).value()== tileValue && leftMeld.tiles().get(i).color() == tileColor){
                    //this is the number of tiles that we can detach
                    if(i >= 2){
                        map.put(leftMeld, i);
                        return map;
                    }
                }
            }
        }
        return map;
    }
}
