package project.rummy.ai;

import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.TableData;
import project.rummy.entities.Tile;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.HashSet;
import java.util.Set;

public class SmartStateAnalyzer implements Observer {
  private GameState state;

  private int[] tableTileCounts;
  private int[] handTileCounts;

  public SmartStateAnalyzer(Game game) {
    game.registerObserver(this);
    tableTileCounts = new int[52];
    handTileCounts = new int[52];
  }

  public SmartStateAnalyzer(GameState state) {
    tableTileCounts = new int[52];
    handTileCounts = new int[52];
    this.state = state;
    analyzeCurrentHand();
    analyzeTableState();
  }

  private static int calculatePosition(Tile tile) {
    return tile.value() + tile.color().value() * 13 - 1;
  }

  private void analyzeTableState() {
    tableTileCounts = new int[52];
    TableData tableData = state.getTableData();
    for (Meld meld : tableData.melds) {
      for (Tile tile : meld.tiles()) {
        tableTileCounts[calculatePosition(tile)]++;
      }
    }
  }

  private void analyzeCurrentHand() {
    HandData handData = state.getHandsData()[state.getCurrentPlayer()];
    handData.tiles.forEach(tile -> {
      handTileCounts[calculatePosition(tile)]++;
    });
  }

  public boolean shouldPlay(Tile tile) {
    return false;
  }

  public boolean isPartOfRun(Tile tile) {
    int pos = calculatePosition(tile);
    return (handTileCounts[pos+1] > 0 && handTileCounts[pos+2] >0)
        || (handTileCounts[pos-1] > 0 && handTileCounts[pos+1] >0)
        || (handTileCounts[pos-1] > 0 && handTileCounts[pos-2] >0);
  }

  public boolean isPartOfSet(Tile tile) {
    int value = tile.value();
    int tileCount = 0;
    for (int i = 0; i<4; i++) {
      if (handTileCounts[i * 13 + value - 1] > 0)  {
        tileCount++;
      }
    }
    return tileCount >= 3;
  }

  public boolean shouldWaitForSet(Tile tile) {
    Set<Integer> set = new HashSet<>();
    int value = tile.value();
    for (int i = 0; i<4; i++) {
      int pos = i * 13 + value -1;
      if (handTileCounts[pos] > 0)  {
        set.add(pos);
      }
    }
    if (set.size() == 1) {
      return false;
    } if (set.size() == 2) {
      int tablePoints = 0;
      for (int i = 0; i<4; i++) {
        int pos = i * 13 + value -1;
        if (handTileCounts[pos] == 0) {
          tablePoints += tableTileCounts[pos];
        }
      }
      return tablePoints <= 2;
    }
    return true;
  }

  public boolean shouldWaitForRun(Tile tile) {
    if (isPartOfRun(tile)) {
      return true;
    }
    int pos = tile.value() + tile.color().value() * 13 - 1;
    if (handTileCounts[pos+1] > 0) {
      return tableTileCounts[pos+2] + tableTileCounts[pos-1] <= 2;
    }
    if (handTileCounts[pos-1] > 0) {
      return tableTileCounts[pos+1] + tableTileCounts[pos-2] <= 2;
    }

    if (handTileCounts[pos+2] > 0) {
      return tableTileCounts[pos+1] + tableTileCounts[pos-2] <= 2;
    }
    return false;
  }

  @Override
  public void update(GameState status) {
    this.state = status;
  }
}
