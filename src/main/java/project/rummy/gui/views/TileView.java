package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class TileView extends Pane {
  private GameFXMLLoader loader;

  @FXML private Text value;

  public TileView(Tile tile) {
    super();
    this.loader = new GameFXMLLoader("tile");
    loader.setController(this);
    loadTileView(tile);
  }

  private void loadTileView(Tile tile) {
    Node tileView;
    try {
      tileView = loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Can not load tile");
    }
    value.setText(String.valueOf(tile.value()));
    value.setFill(getColor(tile.color()));
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
      default:
        return javafx.scene.paint.Color.GREEN;
    }
  }

}
