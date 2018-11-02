package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.ArrayList;
import java.util.List;

public class TableData extends Component {
  public List<Tile> freeTiles;
  public List<Meld> melds;
  public int[][] setGrid1;
  public int[][] setGrid2;
  public int[][] runGrid;

  public TableData(Table table) {
    freeTiles = table.getFreeTiles();
    melds = table.getPlayingMelds();
    setGrid1 = table.getSetGrid1();
    setGrid2 = table.getSetGrid2();
    runGrid = table.getRunGrid();
  }

  public TableData() {
    freeTiles = new ArrayList<>();
    melds = new ArrayList<>();
    setGrid1 = new int[13][4];
    setGrid2 = new int[13][4];
    runGrid = new int[8][13];
  }

  public static Table toTable(TableData data) {
    return new Table(data.melds, data.freeTiles, data.setGrid1, data.setGrid2, data.runGrid);
  }
}
