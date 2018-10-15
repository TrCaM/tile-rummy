package project.rummy.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

/**
 * {@link ManipulationTable} is a temporary holder of melds that will be create each turn. During a turn, this table is
 * the place to store all the melds that need to be modified from the table, melds that going to play from hand. Unlike
 * {@link Table} where melds must not be modified, we can split and combine melds in this table freely during the turn.
 * However, when the turn is finished, this table must contain only valid melds that is playable to the
 * table, so that all the melds that a player borrows from table to manipulate are returned on the table.
 */
public class ManipulationTable {
    private List<Meld> melds;

    public ManipulationTable() {
        melds = new ArrayList<>();
    }

    public void add(Meld ...melds) {
        this.melds.addAll(Arrays.asList(melds));
    }

    public List<Meld> getMelds() {
        return Collections.unmodifiableList(melds);
    }
    /**
     * Remove a meld from this temporary table. Note that a meld should be in it original form since when it was added to
     * be able to be removed.
     */
    public Meld remove(int meldIndex) {
        //TODO: Write test and implement, note that we need to check if the MeldSource is not MANIPULATION for a meld to be
        // able to be removed
        return melds.remove(meldIndex);
    }

    /**
     * split the meld into small parts, decided by the list of breakPoints.
     * after splitting, the original meld will be removed, and new melds will be added to the end of list
     * Throw {@link IllegalArgumentException} if any of the passed in {@code breakPoints} is invalid.
     * @param meldIndex the index of the meld to be split
     * @param breakPoints the index of the right-hand meld.
     */
    public void split(int meldIndex, int ...breakPoints) {

        int meldSize = melds.get(meldIndex).tiles().size();
        int numBreakPoints = breakPoints.length;

        ///checking for invalid breakpoints
        for(int i=0; i<numBreakPoints; i++){
            if(breakPoints[i] <= 0 || breakPoints[i] >= meldSize){
                throw new IllegalArgumentException("Invalid breakpoint");
            }
            for(int h=i+1; h<numBreakPoints; h++){
                if(breakPoints[i] == breakPoints[h]){
                    throw new IllegalArgumentException("Invalid breakpoint");
                }
            }
        }

        List<Meld> meldsList = new ArrayList<>();

        List<Tile> tilesList = melds.get(meldIndex).tiles();
        List<Tile> temp;

        temp = tilesList.subList(0, breakPoints[0]);
        meldsList.add(Meld.createMeld(temp.toArray(new Tile[temp.size()])));

        for(int i=1; i<numBreakPoints; i++){
            temp = tilesList.subList(breakPoints[i-1], breakPoints[i]);
            meldsList.add(Meld.createMeld(temp.toArray(new Tile[temp.size()])));
        }

        temp = tilesList.subList(breakPoints[numBreakPoints-1],meldSize);
        meldsList.add(Meld.createMeld(temp.toArray(new Tile[temp.size()])));

        remove(meldIndex);

        add(meldsList.toArray(new Meld[meldsList.size()]));
    }

  /**
   * Combine melds into a single big meld.
   * after combining, the original melds will be removed, and new meld will be added to the end of list
   * @param meldIndexes the indexes of the melds to be combined.
   */
  public void combineMelds(int ...meldIndexes) {

      Arrays.sort(meldIndexes);
      List<Tile> tilesFromMelds = new ArrayList<>();


      //checking for invalid indexes
      if(meldIndexes.length < 2){
          throw new IllegalArgumentException("Invalid indexes input");
      }

      for(int i=0; i<meldIndexes.length; i++){
          if(meldIndexes[i] < 0 || meldIndexes[i] >= melds.size()){
              throw new IllegalArgumentException("Invalid indexes input");
          }
          for(int k = i+1; k<meldIndexes.length; k++){
              if(meldIndexes[k] == meldIndexes[i]){
                  throw new IllegalArgumentException("Invalid indexes input");
              }
          }

          //add tiles from melds to a list of tiles
          tilesFromMelds.addAll(melds.get(meldIndexes[i]).tiles());
      }

      //sort list of tiles in increasing order
      tilesFromMelds.sort(Comparator.comparing(Tile:: value));

      //create new meld from those tiles
      Meld newMeld = Meld.createMeld(tilesFromMelds.toArray(new Tile[tilesFromMelds.size()]));

      if(newMeld.isValidMeld()) {
          for (int i = meldIndexes.length-1; i >= 0; i--) {
              remove(meldIndexes[i]);
          }
          melds.add(newMeld);
      }else{
          throw new IllegalArgumentException("Invalid MELD");
      }
  }

  /**
   * Submit the melds to the table, if all melds are valid (has more than 3 tiles). Otherwise return
   * false.
   */
  public boolean submit(Table table) {
    throw new UnsupportedOperationException();
  }
}
