package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import project.rummy.entities.HandData;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class HandView extends Pane {
  private GameFXMLLoader loader;

  @FXML private FlowPane tileRack;
  @FXML private FlowPane meldRack;

  public HandView(HandData data) {
    super();
    this.loader = new GameFXMLLoader("hand");
    loader.setController(this);
    loadHandView(data);
  }

  private void loadHandView(HandData data) {
    Node handView;
    try {
      handView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load hand");
    }
    data.tiles.stream()
        .map(TileView::new)
        .forEach(view -> tileRack.getChildren().add(view));
    data.melds.stream()
        .map(MeldView::new)
        .forEach(view -> meldRack.getChildren().add(view));
    getChildren().setAll(handView);
  }

}
