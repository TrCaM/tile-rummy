package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import project.rummy.entities.Tile;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class TileView extends Pane {
  private GameFXMLLoader loader;

  public TileView(Tile tile) {
    super();
    this.loader = new GameFXMLLoader("tile");
    loadTileView(tile);
  }

  private void loadTileView(Tile tile) {
    try {
      Node tileView = loader.load();
      getChildren().setAll(tileView);
    } catch (IOException e) {
      throw new IllegalStateException("Can not load tile");
    }
  }

}
