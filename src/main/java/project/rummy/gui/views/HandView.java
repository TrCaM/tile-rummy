package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;
import project.rummy.events.TileChooseEvent;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HandView extends Pane {
  private GameFXMLLoader loader;

  @FXML private FlowPane tileRack;
  @FXML private FlowPane meldRack;

  Set<Tile> chosenTiles;
  Set<Meld> chosenMelds;

  @FXML private Button formMeldButton;
  @FXML private Button playMeldButton;

  public HandView(HandData data) {
    super();
    this.loader = new GameFXMLLoader("hand");
    loader.setController(this);
    this.chosenTiles = new HashSet<>();
    loadHandView(data);
    setId("hand");
    setUpHandlers();
  }

  private void setUpHandlers() {
    this.addEventHandler(TileChooseEvent.TILE_CHOSEN, this::onTileClick);
  }

  private void onTileClick(TileChooseEvent event) {
    if (event.isChoosing()) {
      chosenTiles.add(event.getTile());
    } else {
      chosenTiles.remove(event.getTile());
    }
    Tile[] tiles = chosenTiles.toArray(new Tile[0]);
    formMeldButton.setDisable(!Meld.canFormMeld(tiles));
    playMeldButton.setDisable(!Meld.canPlayOnTable(tiles));
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
        .map(tile -> new TileView(tile, TileSource.HAND))
        .forEach(view -> {
          tileRack.getChildren().add(view);
        });
    data.melds.stream()
        .map(MeldView::new)
        .forEach(view -> {
          meldRack.getChildren().add(view);
        });
    getChildren().setAll(handView);
  }

}
