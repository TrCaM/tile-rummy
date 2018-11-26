package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import project.rummy.entities.HandData;
import project.rummy.entities.Player;
import project.rummy.entities.TileSource;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;

public class GameInfoView extends Pane implements Observer {
  private GameFXMLLoader loader;

  @FXML private Label humanTiles;
  @FXML private Label oppo1Tiles;
  @FXML private Label oppo2Tiles;
  @FXML private Label oppo3Tiles;
  @FXML private Label status0;
  @FXML private Label status1;
  @FXML private Label status2;
  @FXML private Label status3;
  @FXML private Label freeTiles;
  @FXML private Label turnNum;
  @FXML private Label you;
  @FXML private Label oppo1;
  @FXML private Label oppo2;
  @FXML private Label oppo3;
  @FXML private FlowPane oppo1Hand;
  @FXML private FlowPane oppo2Hand;
  @FXML private FlowPane oppo3Hand;
  @FXML private Node oppoHands;
  @FXML private Button debugButton;

  private int playerId;

  private boolean debugMode;

  public GameInfoView(Player controlledPlayer, GameState gameState) {
    super();
    this.loader = new GameFXMLLoader("gameInfo");
    loader.setController(this);
    loadGameInfoView(gameState);
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
    this.debugMode = false;
    setUpHandlers();
    this.playerId = controlledPlayer.getId();
  }

  private void setUpHandlers() {
    this.debugButton.setOnMouseClicked(event -> {
      debugMode = !debugMode;
      oppoHands.setVisible(debugMode);
    });
  }

  private void loadGameInfoView(GameState gameState) {
    Node gameStateView;
    try {
      gameStateView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load table");
    }
    update(gameState);
    getChildren().setAll(gameStateView);
  }

  private void renderGameInfo(GameState gameState) {
    // Update the num tiles

    oppo1Tiles.setText("" + gameState.getHandsData()[1].tiles.size() + " Tiles");
    oppo2Tiles.setText("" + gameState.getHandsData()[2].tiles.size() + " Tiles");
    oppo3Tiles.setText("" + gameState.getHandsData()[3].tiles.size() + " Tiles");
    humanTiles.setText("" + gameState.getHandsData()[0].tiles.size() + " Tiles");
    // Update current player
    you.setText(gameState.getPlayerData()[0].name);
    oppo1.setText(gameState.getPlayerData()[1].name);
    oppo2.setText(gameState.getPlayerData()[2].name);
    oppo3.setText(gameState.getPlayerData()[3].name);
    Label currentLabel;
    you.getStyleClass().clear();
    oppo1.getStyleClass().clear();
    oppo2.getStyleClass().clear();
    oppo3.getStyleClass().clear();
    switch (gameState.getCurrentPlayer()) {
      case 0:
        currentLabel = you;
        break;
      case 1:
        currentLabel = oppo1;
        break;
      case 2:
        currentLabel = oppo2;
        break;
      default:
        currentLabel = oppo3;
    }
    currentLabel.getStyleClass().add("current");
    // Update status
    status0.setText(gameState.getPlayerStatuses()[0].toString());
    status1.setText(gameState.getPlayerStatuses()[1].toString());
    status2.setText(gameState.getPlayerStatuses()[2].toString());
    status3.setText(gameState.getPlayerStatuses()[3].toString());
    // Update the rest
    freeTiles.setText(Integer.toString(gameState.getFreeTilesCount()));
    turnNum.setText(Integer.toString(gameState.getTurnNumber()));
  }

  @Override
  public void update(GameState status) {
    renderGameInfo(status);
    loadOpponentHand(status);
  }

  private void loadOpponentHand(GameState status) {
    HandData[] data = new HandData[4];
    int current = 1;
    for (int i=0; i<4; i++) {
      if (i == playerId) {
        data[0] = status.getHandsData()[i];
      } else {
        data[current++] = status.getHandsData()[i];
      }
    }
    oppo1Hand.getChildren().clear();
    oppo2Hand.getChildren().clear();
    oppo3Hand.getChildren().clear();
    data[1].tiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND, 0, data[1].tiles.indexOf(tile)))
        .forEach(view -> oppo1Hand.getChildren().add(view));
    data[2].tiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND, 0, data[2].tiles.indexOf(tile)))
        .forEach(view -> oppo2Hand.getChildren().add(view));
    data[3].tiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND, 0, data[3].tiles.indexOf(tile)))
        .forEach(view -> oppo3Hand.getChildren().add(view));
    oppoHands.setVisible(debugMode);
  }
}


