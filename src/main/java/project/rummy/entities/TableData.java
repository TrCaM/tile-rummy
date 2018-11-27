package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData extends Component implements Serializable {
  public List<Tile> freeTiles;
  public List<Meld> melds;
  public int[][] setGrid1;
  public int[][] setGrid2;
  public int[][] runGrid;
  public Map<Integer, Meld> meldMap;

  public TableData(Table table) {
    freeTiles = table.getFreeTiles();
    melds = table.getPlayingMelds();
    setGrid1 = table.getSetGrid1();
    setGrid2 = table.getSetGrid2();
    runGrid = table.getRunGrid();
    meldMap = buildMeldMap(table.getPlayingMelds());
  }

  private Map<Integer, Meld> buildMeldMap(List<Meld> melds) {
    Map<Integer, Meld> map = new HashMap<>();
    melds.forEach(meld -> map.put(meld.getId(), meld));
    return map;
  }

  public TableData() {
    freeTiles = new ArrayList<>();
    melds = new ArrayList<>();
    setGrid1 = new int[13][4];
    setGrid2 = new int[13][4];
    runGrid = new int[13][13];
  }

  public static Table toTable(TableData data) {
    return new Table(data.melds, data.freeTiles, data.setGrid1, data.setGrid2, data.runGrid);
  }
}
