package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;
import project.rummy.events.TileChooseEvent;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class TileView extends Pane {
  private GameFXMLLoader loader;
  private Tile tile;
  private boolean isChosen;
  private TileSource tileSource;
  private int row;
  private int col;

  @FXML private Text value;
  @FXML private ImageView face;
  @FXML private Rectangle border;

  TileView(Tile tile, TileSource tileSource, int row, int col) {
    super();
    this.tile = tile;
    this.tileSource = tileSource;
    this.isChosen = false;
    this.loader = new GameFXMLLoader("tile");
    this.row = row;
    this.col = col;
    loader.setController(this);
    loadTileView(tile);
    setUpHandlers();
  }

  public Tile getTile() {
    return tile;
  }

  private void setUpHandlers() {
    this.addEventHandler(MouseEvent.MOUSE_CLICKED,this::onTileClicked);
  }

  private void loadTileView(Tile tile) {
    Node tileView;
    try {
      tileView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load tile");
    }
    value.setText(tile.toSymbol());
    if (tile.isJoker()) {
      value.setVisible(false);
      face.setVisible(true);
      border.setStroke(javafx.scene.paint.Color.rgb(164, 14, 14));
    }
    value.setFill(getColor(tile.color()));

    if(tile.isSuggested() && tile.isHightlight()){
      border.setStroke(javafx.scene.paint.Color.rgb(86, 244, 65));
    }else {
      if (tile.isHightlight()) {
        border.getStyleClass().add("highlight");
      }
      if (tile.isSuggested()) {
        border.setStroke(javafx.scene.paint.Color.rgb(86, 244, 65));
      }
    }




    getChildren().setAll(tileView);
  }

  private static javafx.scene.paint.Color getColor(Color color) {
    switch (color) {
      case RED:
        return javafx.scene.paint.Color.RED;
      case BLACK:
        return javafx.scene.paint.Color.BLACK;
      case ORANGE:
        return javafx.scene.paint.Color.ORANGE;
      case GREEN:
        return javafx.scene.paint.Color.GREEN;
      default:
        return javafx.scene.paint.Color.PEACHPUFF;
    }
  }

  private void onTileClicked(MouseEvent event) {
    toggleChosen(!isChosen);
    this.fireEvent(new TileChooseEvent(tile, tileSource, isChosen, row, col));
    event.consume();
  }

  void toggleChosen(boolean isChosen) {
    this.isChosen = isChosen;
    if (isChosen) {
      border.getStyleClass().add("chosen");
    } else {
      border.getStyleClass().clear();
    }
  }

  TileSource getTileSource() {
    return tileSource;
  }

  int getRow() {
    return row;
  }

  int getCol() {
    return col;
  }

}
