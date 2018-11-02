package project.rummy.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class represent the table entity of the game
 */
public class Table {

  private List<Tile> freeTiles;
  private List<Meld> melds;
  private int[][] setGrid1;
  private int[][] setGrid2;
  private int[][] runGrid;
  /**
   * Note for back up melds: It is used for restoring the table before each turn.
   */
  private List<Meld> backupMelds;

  static final int MAX_VALUE = 13;
  static final int DECKS_AMOUNT = 2;

  public Table() {
    this.melds = new ArrayList<>();
    this.backupMelds = new ArrayList<>();
    this.freeTiles = new ArrayList<>();
    this.setGrid1 = new int[13][4];
    this.setGrid2 = new int[13][4];
    this.runGrid = new int[13][13];
  }


  public Table(List<Meld> melds, List<Tile> freeTiles, int[][] setGrid1, int[][] setGrid2, int[][] runGrid) {

    this.melds = new ArrayList<>(melds);
    this.backupMelds = new ArrayList<>();
    this.freeTiles = new ArrayList<>(freeTiles);
    this.setGrid1 = copy2DimensionArray(setGrid1);
    this.setGrid2 = copy2DimensionArray(setGrid2);
    this.runGrid = copy2DimensionArray(runGrid);
  }


    private static int[][] copy2DimensionArray(int[][] old) {
      int[][] current = new int[old.length][old[0].length];
      for(int i=0; i<old.length; i++) {
        for(int j=0; j<old[i].length; j++) {
          current[i][j]=old[i][j];
        }
      }
      return current;
    }

    public int[][] getSetGrid1() {
//      return setGrid1;
      return copy2DimensionArray(setGrid1);
    }

    public int[][] getSetGrid2() {
//      return setGrid2;
      return copy2DimensionArray(setGrid2);
    }

    public int[][] getRunGrid() {
//      return runGrid;
      return copy2DimensionArray(runGrid);
    }


  public Meld getMeldById(int meldid){
    for(Meld m: melds){
      if(m.getId() == meldid){
        return m;
      }
    }
    return null;
  }

  public void initTiles() {
    createDeck();
  }


    public void shuffle() {
        Collections.shuffle(freeTiles);
    }

    private void createDeck() {
        for (int j = 0; j < DECKS_AMOUNT; j++) {
            for (Color color : Color.values()) {
                for (int i = 1; i <= MAX_VALUE; i++) {
                    this.freeTiles.add(Tile.createTile(color, i));
                }
            }
        }
    }

    /**
     * Storing the current list of melds so that we can restore the table as it was at the beginning
     * of each turn.
     */
    public void backupMelds() {
        for (Meld meld : melds) {
            Tile[] tiles = new Tile[meld.tiles().size()];
            meld.tiles().toArray(tiles);
            backupMelds.add(Meld.createMeld(tiles));
        }
    }

    /**
     * Restore the state of the table as in the backup melds.
     */
    public void restoreFromBackup() {
        this.melds = backupMelds;
    }


    public List<Tile> getFreeTiles() {
        return new ArrayList<>(freeTiles);
    }

    public int getFreeTilesSize() {
        return freeTiles.size();
    }

    public List<Meld> getPlayingMelds() {
        return new ArrayList<>(melds);
    }

    public List<Meld> getBackupMelds() {
        return new ArrayList<>(backupMelds);
    }

    /**
     * Add meld into the table if the input meld is a valid meld and return true. Otherwise not add
     * and return false.
     */
    public boolean addMeld(Meld meld) {
        if (!meld.isValidMeld()) {
            return false;
        }
        setPosition(meld);
        melds.add(meld);
        return true;
    }

    private void setPosition(Meld meld) {
        System.out.println(meld.type() + meld.tiles().toString());
        if (meld.type() == MeldType.RUN) {
            setPositionForRun(meld);
        } else {
            setPositionForSet(meld);
        }
    }

    private void setPositionForRun(Meld meld) {
        if (meld.type() != MeldType.RUN) {
            throw new IllegalStateException("Expected a Run, not other types of Meld");
        }
        int color = meld.getTile(0).color().value();
        int row = color * 2;
        for (Tile tile : meld.tiles()) {
            if (runGrid[row][tile.value() - 1] != 0) {
                row = color * 2 + 1;
                break;
            }
        }
        for (Tile tile : meld.tiles()) {
            runGrid[row][tile.value() - 1] = meld.getId();
        }
        System.out.println();
    }

    private void setPositionForSet(Meld setMeld) {
        if (setMeld.type() != MeldType.SET) {
            throw new IllegalStateException("Expected a Set, not other types of Meld");
        }
        int row = setMeld.getTile(0).value() - 1;
//    if (setGrid[value *2][0] == 0 && setGrid[value *2 +1][0] != 0) {
//      throw new IllegalStateException("Unexpected grid overlap");
//    }
//    int row = setGrid[value * 2] == null ? value*2 : (value*2 +1);
//    for (int i=0; i<setMeld.tiles().size(); i++) {
//      setGrid[row][i] = setMeld.getId();
//    }

        if (setGrid1[row][0] == 0 && setGrid1[row][1] == 0) {
            for (int i = 0; i < setMeld.tiles().size(); i++) {
                setGrid1[row][i] = setMeld.getId();
            }
        } else {
            for (int i = 0; i < setMeld.tiles().size(); i++) {
                setGrid2[row][i] = setMeld.getId();
            }
        }
    }

    public Meld removeMeld(Meld meld) {
        return removeMeld(melds.indexOf(meld));
    }

    public Meld removeMeld(int index) {
        Meld meld = melds.get(index);

        if (meld.type() == MeldType.SET) {
            int row = meld.getTile(0).value() - 1;

            if (setGrid1[row][0] == meld.getId()) {
                for (int i = 0; i < 4; i++) {
                    setGrid1[row][i] = 0;
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    setGrid2[row][i] = 0;
                }
            }
        } else {
            int row = meld.getTile(0).color().value() * 2;
            int value = meld.getTile(0).value();
            if (runGrid[row][value] != meld.getId()) {
                row++;
            }
            if (runGrid[row][value] != meld.getId()) {
                throw new IllegalStateException("Grid error");
            }
            for (int i = 0; i < 13; i++) {
                if (runGrid[row][i] == meld.getId()) {
                    runGrid[row][i] = 0;
                }
            }
        }
        return melds.remove(index);
    }

    public Tile drawTile() {
        return freeTiles.remove(freeTiles.size() - 1);
    }

  public TableData toTableData() {
    return new TableData(this);
  }
}
