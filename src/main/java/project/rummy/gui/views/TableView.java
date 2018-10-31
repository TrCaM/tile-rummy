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

  @FXML private GridPane setPane;
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
//    tableData.stream()
//        .filter(meld -> meld.getTableRow() != -1)
//        .forEach(this::renderMeld);
//    tableData.stream()
//        .filter(meld -> meld.getTableRow() == -1)
//        .forEach(meld -> {
//          findAvailableRow
//        });
//    for (Meld meld : tableData) {
//      if (meld.getTableRow() != -1) {
//        renderMeld(meld, );
//      }
//    }
  }

  private void renderTile(Tile tile, GridPane pane, int row, int col) {
    pane.add(new TileView(tile), row, col);
  }

  private void renderMeld(Meld meld) {
    int row = meld.getTableRow();
    GridPane pane = meld.type() == MeldType.RUN ? runPane : setPane;
    IntStream.range(0, meld.tiles().size())
        .forEach(i -> pane.add(new TileView(meld.getTile(i)), meld.getTableRow(), i ));
  }

}

