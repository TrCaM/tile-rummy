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
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HandView extends Pane implements Observer {
  private GameFXMLLoader loader;

  private Node handView;
  private TurnStatus turnStatus;

  @FXML
  private FlowPane tileRack;
  @FXML
  private FlowPane meldRack;
  @FXML

  Set<TileView> chosenTiles;
  Set<Meld> chosenMelds;

  @FXML
  private Button formMeldButton;
  @FXML
  private Button playMeldButton;
  @FXML
  private Button drawButton;
  @FXML
  private Button nextTurnButton;
  @FXML
  private Button undoButton;

  public HandView(GameState state) {
    super();
    this.loader = new GameFXMLLoader("hand");
    loader.setController(this);
    this.chosenTiles = new HashSet<>();
    this.turnStatus = state.getTurnStatus();
    loadHandView(state);
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
    this.nextTurnButton.setOnMouseClicked(this::onNextTurnButtonClick);
  }

  private void onNextTurnButtonClick(MouseEvent mouseEvent) {
    CommandProcessor.getInstance().enqueueCommand(ActionHandler::nextTurn);
  }

  private void onPlayMeldButtonClick(MouseEvent mouseEvent) {
    int[] handTileIndexes = chosenTiles.stream()
        .filter(tileView -> tileView.getTileSource() == TileSource.HAND)
        .mapToInt(TileView::getCol)
        .toArray();
    Map<Meld, List<Integer>> detachTiles = new HashMap<>();
    chosenTiles.stream()
        .filter(tileView -> tileView.getTileSource() == TileSource.HAND_MELD)
        .forEach(tileView -> {
          Meld meld = Meld.idsToMelds.get(tileView.getRow());
          if (!detachTiles.containsKey(meld)) {
            detachTiles.put(meld, new ArrayList<>());
          }
          detachTiles.get(meld).add(tileView.getCol());
        });
    CommandProcessor.getInstance().enqueueCommand(handler -> {
      ManipulationTable manipulationTable = handler.getManipulationTable();
      List<Integer> combineMelds = new ArrayList<>();
      detachTiles.forEach((key, value) -> {
        int[] splitAll = IntStream.range(1, key.tiles().size()).toArray();
        List<Integer> meldIds = manipulationTable.split(key, splitAll);
        value.forEach(index -> combineMelds.add(meldIds.get(index)));
      });
      handler.formMeld(handTileIndexes);
      handler
          .playAllMeldFromHand()
          .forEach(meld -> combineMelds.add(meld.getId()));
      Meld newMeld = manipulationTable.combineMelds(combineMelds);
      handler.submit(newMeld);
    });
  }

  private void onUndoButtonClick(MouseEvent mouseEvent) {
    CommandProcessor.getInstance().enqueueCommand(ActionHandler::restoreTurn);
  }

  private void onTileClick(TileChooseEvent event) {
    if (event.isChoosing()) {
      chosenTiles.add((TileView) event.getTarget());
    } else {
      chosenTiles.remove(event.getTarget());
    }
    Tile[] tiles = chosenTiles.stream().map(TileView::getTile).toArray(Tile[]::new);
    formMeldButton.setDisable(!Meld.canFormMeld(tiles));
    playMeldButton.setDisable(!turnStatus.canPlay || !Meld.canPlayOnTable(tiles));
  }

  private void onDrawClick(MouseEvent event) {
    CommandProcessor.getInstance().enqueueCommand(ActionHandler::draw);
  }

  private void loadHandView(GameState state) {
    try {
      handView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load hand");
    }
    update(state);
    getChildren().setAll(handView);
  }

  @Override
  public void update(GameState status) {
    HandData data = status.getHandsData()[0];
    this.chosenTiles.clear();
    tileRack.getChildren().clear();
    meldRack.getChildren().clear();
    data.tiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND, 0, data.tiles.indexOf(tile)))
        .forEach(view -> tileRack.getChildren().add(view));
    List<Meld> manipulatingMelds = ManipulationTable.getInstance().getMelds();
    manipulatingMelds.stream()
        .map(MeldView::new)
        .forEach(view -> meldRack.getChildren().add(view));
    turnStatus = status.getTurnStatus();
    drawButton.setDisable(!turnStatus.canDraw);
    playMeldButton.setDisable(true);
    this.tileRack.setDisable(!turnStatus.canPlay);
    this.meldRack.setDisable(!turnStatus.canPlay);
    this.nextTurnButton.setDisable(!turnStatus.canEnd);
    this.undoButton.setDisable(status.getCurrentPlayer() != 0);
  }
}
