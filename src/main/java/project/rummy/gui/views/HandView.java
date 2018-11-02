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
import project.rummy.entities.*;
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
  private TurnStatus turnStatus;

  @FXML
  private FlowPane tileRack;
  @FXML
  private FlowPane meldRack;
  @FXML

  Set<Tile> chosenTiles;
  Set<Meld> chosenMelds;

  @FXML
  private Button formMeldButton;
  @FXML
  private Button playMeldButton;
  @FXML
  private Button drawButton;
  @FXML
  private Button endTurnButton;
  @FXML
  private Button undoButton;

  public HandView(GameState state) {
    super();
    this.loader = new GameFXMLLoader("hand");
    loader.setController(this);
    this.chosenTiles = new HashSet<>();
    this.turnStatus = state.getTurnStatus();
    loadHandView(state.getHandsData()[0]);
    setId("hand");
    setUpHandlers();
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
  }

  private void setUpHandlers() {
    this.addEventHandler(TileChooseEvent.TILE_CHOSEN, this::onTileClick);
    this.drawButton.setOnMouseClicked(this::onDrawClick);
    this.playMeldButton.setOnMouseClicked(this::onPlayMeldButtonClick);
    this.undoButton.setOnMouseClicked(this::onUndoButtonClick);
  }

  private void onPlayMeldButtonClick(MouseEvent mouseEvent) {
    CommandProcessor.getInstance().enqueueCommand(handler -> {
      int[] indexes = chosenTiles.stream()
          .mapToInt(tile -> handler.getHand().getTiles().indexOf(tile))
          .toArray();
      handler.formMeld(indexes);
      handler.playFromHand(0);
      handler.submit();
    });
  }

  private void onUndoButtonClick(MouseEvent mouseEvent) {
    CommandProcessor.getInstance().enqueueCommand(ActionHandler::restoreTurn);
  }

  private void onTileClick(TileChooseEvent event) {
    if (event.isChoosing()) {
      chosenTiles.add(event.getTile());
    } else {
      chosenTiles.remove(event.getTile());
    }
    Tile[] tiles = chosenTiles.toArray(new Tile[chosenTiles.size()]);
    formMeldButton.setDisable(!Meld.canFormMeld(tiles));
    playMeldButton.setDisable(!turnStatus.canPlay || !Meld.canPlayOnTable(tiles));
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
    System.out.println("Hand view updated");
    HandData data = status.getHandsData()[0];
    this.chosenTiles.clear();
    tileRack.getChildren().clear();
    meldRack.getChildren().clear();
    data.tiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND))
        .forEach(view -> tileRack.getChildren().add(view));
    data.melds.stream().map(MeldView::new).forEach(view -> meldRack.getChildren().add(view));
    turnStatus = status.getTurnStatus();
    drawButton.setDisable(!turnStatus.canDraw);
    playMeldButton.setDisable(true);
    this.tileRack.setDisable(!turnStatus.canPlay);
    this.meldRack.setDisable(!turnStatus.canPlay);
  }
}
