
package project.rummy.ai;

import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

import java.util.*;

public class HandMeldSeeker {

    /**
     * find all possible sets that can be made up from a list of tiles
     */
    public static List<Meld> findPossibleSets(List<Tile> tiles) {

        List<Meld> possibleSets = new ArrayList<>();
        List<Tile> alist = new ArrayList<>(tiles);

        while (alist.size() > 2) {
            List<Tile> tempMeld = new ArrayList<>();
            tempMeld.add(alist.get(0));

            for (int i = 1; i < alist.size(); i++) {
                if (alist.get(0).value() == alist.get(i).value()
                        && alist.get(0).color() != alist.get(i).color()
                        && !tempMeld.contains(alist.get(i))) {
                    tempMeld.add(alist.get(i));
                }
            }

            if (tempMeld.size() >= 3) { possibleSets.add(Meld.createMeld(tempMeld)); }
            tempMeld.forEach(alist::remove);
        }
        return possibleSets;
    }

    /**
     * find all possible runs that can be made up from a list of tiles
     */
    public static List<Meld> findPossibleRuns(List<Tile> tilesList) {
        List<Meld> possibleRuns = new ArrayList<>();
        List<Tile> alist = new ArrayList<>(tilesList);
        alist.sort(Comparator.comparing(Tile::value));

        while (alist.size() > 2) {
            List<Tile> tempMeld = new ArrayList<>();
            tempMeld.add(alist.get(0));

            for (int i = 1; i < alist.size(); i++) {
                if (tempMeld.get(0).color() == alist.get(i).color()
                        && !tempMeld.contains(alist.get(i))
                        && tempMeld.get(tempMeld.size() - 1).value() == alist.get(i).value() - 1) {
                    tempMeld.add(alist.get(i));
                }
            }

            if (tempMeld.size() >= 3) { possibleRuns.add(Meld.createMeld(tempMeld)); }
            tempMeld.forEach(alist::remove);
        }
        return possibleRuns;
    }


    /**
     * find all possible sets and runs from the hand's tiles that give the highest score
     * list of melds 1: find all possible sets first, then runs
     * list of melds 2: find all possible runs first, then sets
     * return the list of melds that gives the highest score
     */
    public static List<Meld> findBestMelds(List<Tile> tiles) {
        List<Tile> tilesList = new ArrayList<>(tiles);

        //sets first then runs
        List<Meld> bestMelds_1 = findPossibleSets(tilesList);



        for(Meld m : bestMelds_1) { m.tiles().forEach(tilesList::remove);}
        bestMelds_1.addAll(findPossibleRuns(tilesList));

        int score_1 = bestMelds_1.stream().mapToInt(Meld::getScore).sum();


        //runs first then sets
        tilesList = new ArrayList<>(tiles);
        List<Meld> bestMelds_2 = findPossibleRuns(tilesList);

        //bestMelds_2.stream().map(meld -> meld.tiles()).forEach(tilesList::remove);

        for(Meld m : bestMelds_2) {
            m.tiles().forEach(tilesList::remove);
        }

        bestMelds_2.addAll(findPossibleSets(tilesList));

        int score_2 = bestMelds_2.stream().mapToInt(Meld::getScore).sum();

        return score_1 > score_2 ? bestMelds_1 : bestMelds_2;
    }

    /**
     * find a meld from a list of tiles that gives the highest score
     */
    public static Meld findNextMelds(List<Tile> tiles) {

        List<Meld> sets = findPossibleSets(tiles);
        List<Meld> runs = findPossibleRuns(tiles);

        if (sets.isEmpty() && runs.isEmpty()) return null;

        Meld maxSet = null;
        int setScore = 0;
        if(!sets.isEmpty()) {
            maxSet = Collections.max(sets, Comparator.comparing(Meld::getScore));
            setScore = maxSet.getScore();
        }


        Meld maxRun = null;
        int runScore = 0;
        if(!runs.isEmpty()) {
            maxRun = Collections.max(runs, Comparator.comparing(Meld::getScore));
            runScore = maxRun.getScore();
        }

        return setScore > runScore ? maxSet : maxRun;
    }

}

