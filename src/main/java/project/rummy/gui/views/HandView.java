package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;
import project.rummy.events.TileChooseEvent;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HandView extends Pane implements Observer {
  private GameFXMLLoader loader;

  private Node handView;

  @FXML private FlowPane tileRack;
  @FXML private FlowPane meldRack;

  Set<Tile> chosenTiles;
  Set<Meld> chosenMelds;

  @FXML private Button formMeldButton;
  @FXML private Button playMeldButton;
  @FXML private Button drawButton;

  public HandView(HandData data) {
    super();
    this.loader = new GameFXMLLoader("hand");
    loader.setController(this);
    this.chosenTiles = new HashSet<>();
    loadHandView(data);
    setId("hand");
    setUpHandlers();
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
  }

  private void setUpHandlers() {
    this.addEventHandler(TileChooseEvent.TILE_CHOSEN, this::onTileClick);
    this.drawButton.setOnMouseClicked(this::onDrawClick);
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

  private void onDrawClick(MouseEvent event) {
    CommandProcessor.getInstance().enqueueCommand(ActionHandler::draw);
  }

  private void loadHandView(HandData data) {
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

  @Override
  public void update(GameState status) {
    HandData data = status.getHandsData()[0];
    tileRack.getChildren().clear();
    meldRack.getChildren().clear();
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
  }
}
