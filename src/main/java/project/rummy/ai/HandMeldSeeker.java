
package project.rummy.ai;

import project.rummy.entities.Color;
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
    alist.sort(Comparator.comparing(Tile::value).reversed());

    while (alist.size() > 2) {
      List<Tile> tempMeld = new ArrayList<>();

      while (alist.get(0).isJoker()) {
        Tile t = alist.get(0);
        alist.remove(0);
        alist.add(t);
      }
      tempMeld.add(alist.get(0));
      List<Color> existedColor = new ArrayList<>();
      existedColor.add(tempMeld.get(0).color());

      for (int i = 1; i < alist.size(); i++) {
        if (alist.get(i).isJoker()) {
          tempMeld.add(alist.get(i));
        } else if (alist.get(0).value() == alist.get(i).value()
            && !existedColor.contains(alist.get(i).color())) {
          existedColor.add(alist.get(i).color());
          tempMeld.add(alist.get(i));
        }

        if (tempMeld.size() == 4) {
          break;
        }
      }

      if (tempMeld.size() >= 3) {
        possibleSets.add(Meld.createMeld(tempMeld));
        tempMeld.forEach(alist::remove);
      } else {
        tempMeld.stream().filter(tile -> !tile.isJoker()).forEach(alist::remove);
      }
    }
    return possibleSets;
  }

  /**
   * find all possible runs that can be made up from a list of tiles
   */
  public static List<Meld> findPossibleRuns(List<Tile> tilesList) {
    List<Meld> possibleRuns = new ArrayList<>();
    List<Tile> alist = new ArrayList<>();
    tilesList.stream()
        .filter(tile -> !tile.isJoker())
        .sorted(Comparator.comparing(Tile::value).reversed())
        .forEach(alist::add);
    tilesList.stream()
        .filter(tile -> tile.isJoker())
        .forEach(alist::add);

    while (alist.size() > 2) {
      List<Tile> tempMeld = new ArrayList<>();
      tempMeld.add(alist.get(0));
      Color color = tempMeld.get(0).color();
      int endValue = tempMeld.get(0).value();

      for (int k = endValue - 1; k >= 1; k--) {
        boolean tileFound = false;
        for (int i = 1; i < alist.size(); i++) {
          if (k == 1) {
            if (alist.get(i).canFillToRun(color, endValue + 1) && !tempMeld.contains(alist.get(i))) {
              tempMeld.add(alist.get(i));
              tileFound = true;
              break;
            }
          } else if (alist.get(i).canFillToRun(color, k) && !tempMeld.contains(alist.get(i))) {
            tempMeld.add(alist.get(i));
            tileFound = true;
            break;
          }
        }
        if (!tileFound) {
          break;
        }
      }

      if (tempMeld.size() >= 3) {
        possibleRuns.add(Meld.createMeld(tempMeld));
        tempMeld.forEach(alist::remove);
      } else {
        tempMeld.stream().filter(tile -> !tile.isJoker()).forEach(alist::remove);
      }
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

    for (Meld m : bestMelds_1) {
      m.tiles().forEach(tilesList::remove);
    }
    bestMelds_1.addAll(findPossibleRuns(tilesList));

    int score_1 = bestMelds_1.stream().mapToInt(Meld::getScore).sum();

    //runs first then sets
    tilesList = new ArrayList<>(tiles);
    List<Meld> bestMelds_2 = findPossibleRuns(tilesList);


    for (Meld m : bestMelds_2) {
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
    if (!sets.isEmpty()) {
      maxSet = Collections.max(sets, Comparator.comparing(Meld::getScore));
      setScore = maxSet.getScore();
    }


    Meld maxRun = null;
    int runScore = 0;
    if (!runs.isEmpty()) {
      maxRun = Collections.max(runs, Comparator.comparing(Meld::getScore));
      runScore = maxRun.getScore();
    }

    return setScore > runScore ? maxSet : maxRun;
  }

  /**
   * find remaining tiles that cannot form melds
   */
  public static List<Tile> findRemainingTiles(List<Tile> tiles) {
    List<Meld> allMelds = findBestMelds(tiles);
    List<Tile> remainingTiles = new ArrayList<>(tiles);
    for (Meld m : allMelds) {
      m.tiles().forEach(remainingTiles::remove);
    }
    return remainingTiles;
  }



}

