package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import project.rummy.entities.Meld;
import project.rummy.entities.MeldType;
import project.rummy.entities.TableData;
import project.rummy.entities.Tile;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class TableView extends Pane {
  private GameFXMLLoader loader;

  @FXML private GridPane setPane1;
  @FXML private GridPane setPane2;
  @FXML private GridPane runPane;

  public TableView(TableData tableData) {
    super();
    this.loader = new GameFXMLLoader("table");
    loader.setController(this);
    loadTableView(tableData);
  }

  private void loadTableView(TableData tableData) {
    Node tableView;
    try {
      tableView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load table");
    }
    renderMelds(tableData);
    getChildren().setAll(tableView);
  }

  private void renderMelds(TableData tableData) {
    int[][] setGrid1 = tableData.setGrid1;
    int[][] setGrid2 = tableData.setGrid2;
    int[][] runGrid = tableData.runGrid;
    for (int row=0; row<13; row++) {
      for(int col=0; col<4; col++) {
        int meldId1 = setGrid1[row][col];
        int meldId2 = setGrid2[row][col];
        if (meldId1 != 0) {
          Meld meld1 = Meld.idsToMelds.get(meldId1);
          renderTile(meld1.getTile(col), setPane1, row, col);
        }
        if (meldId2 != 0) {
          Meld meld2 = Meld.idsToMelds.get(meldId2);
          renderTile(meld2.getTile(col), setPane2, row, col);
        }
      }
    }
    for (int row=0; row<8; row++) {
      for (int col=0; col<13; col++) {
        int runId = runGrid[row][col];
        if (runId != 0) {
          Meld run = Meld.idsToMelds.get(runId);
          int firstVal = run.getTile(0).value();
          renderTile(run.getTile(col +1 - firstVal), runPane, row, col);
        }
      }
    }
  }

  private void renderTile(Tile tile, GridPane pane, int row, int col) {
    pane.add(new TileView(tile), col, row);
  }
}

