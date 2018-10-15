package project.rummy.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
   * Throw {@link IllegalArgumentException} if any of the passed in {@code breakPoints} is invalid.
   * @param meldIndex the index of the meld to be split
   * @param breakPoints the index of the last meld of each partial meld.
   */
  public void split(int meldIndex, int ...breakPoints) {
    throw new UnsupportedOperationException();
  }

  /**
   * Combine melds into a single big meld.
   * @param meldIndexes the indexes of the melds to be combined.
   */
  public void combineMelds(int ...meldIndexes) {
   throw new UnsupportedOperationException();
  }
}
