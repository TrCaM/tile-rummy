package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.List;

public class TableData extends Component {
  public List<Tile> freeTiles;
  public List<Meld> melds;
  public int[][] setGrid1;
  public int[][] setGrid2;
  public int[][] runGrid;

}
