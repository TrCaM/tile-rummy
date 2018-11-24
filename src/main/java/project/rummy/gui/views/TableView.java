package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.*;
import project.rummy.events.TileChooseEvent;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableView extends Pane implements Observer {
  private GameFXMLLoader loader;
  private Node tableView;

  @FXML private GridPane setPane1;
  @FXML private GridPane setPane2;
  @FXML private GridPane runPane;
  @FXML private Button borrowMeldsButton;

  private Set<Meld> chosenMelds;

  private TableData tableData;

  public TableView(Player controlledPlayer, GameState gameState) {
    super();
    this.loader = new GameFXMLLoader("table");
    loader.setController(this);
    this.tableData = gameState.getTableData();
    loadTableView(gameState);
    chosenMelds = new HashSet<>();
    setUpHandlers();

    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
  }

  private void setUpHandlers() {
    this.addEventHandler(TileChooseEvent.TILE_CHOSEN, this::onMeldClick);
    this.borrowMeldsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBorrowMeldsButtonClick);
  }

  private void onBorrowMeldsButtonClick(MouseEvent event) {
    HashSet<Meld> copyOfChosenMelds = new HashSet<>(chosenMelds);
    CommandProcessor.getInstance()
        .enqueueCommand(handler -> handler.takeTableMelds(copyOfChosenMelds));
    chosenMelds.clear();
  }

  private void onMeldClick(TileChooseEvent event) {
    int[][] grid = getGrid(event.getTileSource());
    GridPane pane = getPane(event.getTileSource());
    int meldId = grid[event.getRow()][event.getCol()];
    Meld clickedMeld = Meld.idsToMelds.get(meldId);
    if (event.isChoosing()) {
      chosenMelds.add(clickedMeld);
    } else {
      chosenMelds.remove(clickedMeld);
    }
    List<TileView> tileViews = new ArrayList<>();
    pane.getChildren().forEach(node -> {
      int col = GridPane.getColumnIndex(node);
      int row = GridPane.getRowIndex(node);
      if (grid[row][col] == meldId) {
        tileViews.add((TileView) node);
      }
    });
    tileViews.forEach(tileView -> tileView.toggleChosen(event.isChoosing()));
  }

  private GridPane getPane(TileSource source) {
    switch (source) {
      case TABLE_RUN:
        return runPane;
      case TABLE_JOKER_MELD:
        return runPane;
      case TABLE_SET1:
        return setPane1;
      case TABLE_SET2:
        return setPane2;
      default:
        throw new IllegalStateException("Invalid source");
    }
  }

  private int[][] getGrid(TileSource source) {
    switch (source) {
      case TABLE_JOKER_MELD:
        return tableData.runGrid;
      case TABLE_RUN:
        return tableData.runGrid;
      case TABLE_SET1:
        return tableData.setGrid1;
      case TABLE_SET2:
        return tableData.setGrid2;
      default:
        throw new IllegalStateException("Invalid source");
    }
  }

  private void loadTableView(GameState state) {
    TableData tableData = state.getTableData();
    try {
      tableView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load table");
    }
    renderMelds(tableData);
    getChildren().setAll(tableView);
    update(state);
  }

  private void renderMelds(TableData tableData) {
    int[][] setGrid1 = tableData.setGrid1;
    int[][] setGrid2 = tableData.setGrid2;
    int[][] runGrid = tableData.runGrid;
    // Render sets
    for (int row=0; row<13; row++) {
      for(int col=0; col<4; col++) {
        int meldId1 = setGrid1[row][col];
        int meldId2 = setGrid2[row][col];
        if (meldId1 != 0) {
          Meld meld1 = Meld.idsToMelds.get(meldId1);
          renderTile(meld1.getTile(col), TileSource.TABLE_SET1, setPane1, row, col);
        }
        if (meldId2 != 0) {
          Meld meld2 = Meld.idsToMelds.get(meldId2);
          renderTile(meld2.getTile(col), TileSource.TABLE_SET2, setPane2, row, col);
        }
      }
    }
    // Render melds
    for (int row=0; row<8; row++) {
      for (int col=0; col<13; col++) {
        int runId = runGrid[row][col];
        if (runId != 0) {
          Meld run = Meld.idsToMelds.get(runId);
          int firstVal = run.getTile(0).value();
          renderTile(run.getTile(col +1 - firstVal), TileSource.TABLE_RUN, runPane, row, col);
        }
      }
    }
    // Render Joker melds
    for (int row = 11; row < 13; row++) {
      for (int col=0; col<13; col++) {
        int runId = runGrid[row][col];
        if (runId != 0) {
          Meld jokerMeld = Meld.idsToMelds.get(runId);
          renderTile(jokerMeld.getTile(col), TileSource.TABLE_JOKER_MELD, runPane, row, col);
        }
      }
    }
  }

  private void renderTile(Tile tile, TileSource source, GridPane pane, int row, int col) {
    pane.add(new TileView(tile, source, row, col), col, row);
  }

  @Override
  public void update(GameState state) {
    runPane.getChildren().clear();
    setPane1.getChildren().clear();
    setPane2.getChildren().clear();
    this.tableData = state.getTableData();
    renderMelds(state.getTableData());
    setDisable(state.getPlayerStatuses()[state.getCurrentPlayer()] == PlayerStatus.START);
  }
}

