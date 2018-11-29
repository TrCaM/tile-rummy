package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
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

  @FXML
  private Label player1Tiles;
  @FXML
  private Label player2Tiles;
  @FXML
  private Label player3Tiles;
  @FXML
  private Label player4Tiles;
  @FXML
  private Label status1;
  @FXML
  private Label status2;
  @FXML
  private Label status3;
  @FXML
  private Label status4;
  @FXML
  private Label freeTiles;
  @FXML
  private Label turnNum;
  @FXML
  private Label player1;
  @FXML
  private Label player2;
  @FXML
  private Label player3;
  @FXML
  private Label player4;
  @FXML
  private FlowPane oppo1Hand;
  @FXML
  private FlowPane oppo2Hand;
  @FXML
  private FlowPane oppo3Hand;
  @FXML
  private Node oppoHands;
  @FXML
  private Button debugButton;
  @FXML
  private Label timer;

  private Timeline timeline;
  private int playerId;
  private Integer timeSeconds = 120;


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

    player2Tiles.setText("" + gameState.getHandsData()[1].tiles.size() + " Tiles");
    player3Tiles.setText("" + gameState.getHandsData()[2].tiles.size() + " Tiles");
    player4Tiles.setText("" + gameState.getHandsData()[3].tiles.size() + " Tiles");
    player1Tiles.setText("" + gameState.getHandsData()[0].tiles.size() + " Tiles");
    // Update current player
    player1.setText(gameState.getPlayerData()[0].name);
    player2.setText(gameState.getPlayerData()[1].name);
    player3.setText(gameState.getPlayerData()[2].name);
    player4.setText(gameState.getPlayerData()[3].name);
    Label currentLabel;
    player1.getStyleClass().clear();
    player2.getStyleClass().clear();
    player3.getStyleClass().clear();
    player4.getStyleClass().clear();
    switch (gameState.getCurrentPlayer()) {
      case 0:
        currentLabel = player1;
        break;
      case 1:
        currentLabel = player2;
        break;
      case 2:
        currentLabel = player3;
        break;
      default:
        currentLabel = player4;
    }
    currentLabel.getStyleClass().add("current");
    // Update status
    status1.setText(gameState.getPlayerStatuses()[0].toString());
    status2.setText(gameState.getPlayerStatuses()[1].toString());
    status3.setText(gameState.getPlayerStatuses()[2].toString());
    status4.setText(gameState.getPlayerStatuses()[3].toString());
    // Update the rest
    freeTiles.setText(Integer.toString(gameState.getFreeTilesCount()));
    turnNum.setText(Integer.toString(gameState.getTurnNumber()));
  }

  @Override
  public void update(GameState status) {
    renderGameInfo(status);
    loadOpponentHand(status);
    if (status.isTurnBeginning()) {
      //System.out.println(status.getTurnNumber()  );
      if (timeline != null) {
        timeline.stop();
      }
      timeSeconds = 120;
      timer.setText(timeSeconds.toString());
      timeline = new Timeline();
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.getKeyFrames().add(
          new KeyFrame(Duration.seconds(1), event -> {
                timeSeconds--;
                timer.setText(timeSeconds.toString());
                if (timeSeconds <= 0) {
                  timer.setText("0");
                  if (status.getCurrentPlayer() == playerId) {
                    CommandProcessor.getInstance().enqueueCommand(handler -> {
                      handler.restoreTurn();
                      handler.drawAndEndTurn();
                    });
                    CommandProcessor.getInstance().enqueueCommand(ActionHandler::tryEndTurn);
                  }
                  timeline.stop();
                }
              }));
      timeline.playFromStart();
    }
  }

  private void loadOpponentHand(GameState status) {
    HandData[] data = new HandData[4];
    int current = 1;
    for (int i = 0; i < 4; i++) {
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


