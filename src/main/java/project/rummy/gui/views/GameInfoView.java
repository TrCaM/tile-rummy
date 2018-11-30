package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.util.Arrays;
import java.util.List;

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
  private FlowPane opponent1Hand;
  @FXML
  private FlowPane opponent2Hand;
  @FXML
  private FlowPane opponent3Hand;
  @FXML
  private Node debugArea;
  @FXML
  private Button debugButton;
  @FXML
  private Label timer;
  @FXML
  private FlowPane deckView;
  @FXML
  private ScrollPane scrollPane;

  private Timeline timeline;
  private int playerId;
  private Integer timeSeconds = 120;
  private int playersCount;
  private List<Label> playersTiles;
  private List<FlowPane> opponentsHand;
  private List<Label> playerNames;
  private List<Label> statuses;


  private boolean debugMode;

  GameInfoView(Player controlledPlayer, GameState gameState) {
    super();
    this.playersCount = gameState.getPlayerData().length;
    this.loader = new GameFXMLLoader("gameInfo");
    loader.setController(this);
    loadGameInfoView(gameState);
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.registerObserver(this);
    this.debugMode = false;
    this.playerId = controlledPlayer.getId();
  }

  private void setup() {
    playerNames = Arrays.asList(player1, player2, player3, player4);
    playersTiles = Arrays.asList(player1Tiles, player2Tiles, player3Tiles, player4Tiles);
    opponentsHand = Arrays.asList(opponent1Hand, opponent2Hand, opponent3Hand);
    statuses = Arrays.asList(status1, status2, status3, status4);
    playerNames.forEach(label -> label.setVisible(false));
    playersTiles.forEach(label -> label.setVisible(false));
    playerNames.forEach(label -> label.setVisible(false));
    statuses.forEach(label -> label.setVisible(false));

    playerNames = playerNames.subList(0, playersCount);
    playersTiles = playersTiles.subList(0, playersCount);
    opponentsHand = opponentsHand.subList(0, playersCount - 1);
    statuses = statuses.subList(0, playersCount);
    playerNames.forEach(label -> label.setVisible(true));
    playersTiles.forEach(label -> label.setVisible(true));
    playerNames.forEach(label -> label.setVisible(true));
    statuses.forEach(label -> label.setVisible(true));

    this.debugButton.setOnMouseClicked(event -> {
      debugMode = !debugMode;
      debugArea.setVisible(debugMode);
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
    setup();
    update(gameState);
    getChildren().setAll(gameStateView);
  }

  private void renderGameInfo(GameState gameState) {
    // Update the num tiles

    for (int i = 0;  i < playersCount; i++ ) {
      playersTiles.get(i).setText("" + gameState.getHandsData()[i].tiles.size() + " Tiles");
      playerNames.get(i).setText(gameState.getPlayerData()[i].name);
      playerNames.get(i).getStyleClass().remove("current");
      statuses.get(i).setText(gameState.getPlayerStatuses()[i].toString());
    }
    // Update the rest
    playerNames.get(playerId).getStyleClass().add("you");
    playerNames.get(gameState.getCurrentPlayer()).getStyleClass().add("current");
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
                    CommandProcessor.getInstance().enqueueCommand(ActionHandler::timeOutEndTurn);
                    CommandProcessor.getInstance().enqueueCommand(ActionHandler::tryEndTurn);
                  }
                  timeline.stop();
                }
              }));
      timeline.playFromStart();
    }
  }

  private void loadOpponentHand(GameState status) {
    HandData[] data = new HandData[playersCount];
    int current = 1;
    opponentsHand.forEach(view -> view.getChildren().clear());
    deckView.getChildren().clear();
    for (int i = 0; i < data.length; i++) {
      if (i == playerId) {
        data[0] = status.getHandsData()[i];
      } else {
        data[current] = status.getHandsData()[i];
        current++;
      }
    }

    for (int i = 1; i < data.length; i++) {
      int index = i;
      data[index].tiles.stream()
          .map(tile -> new TileView(tile, TileSource.HAND, 0, data[index].tiles.indexOf(tile)))
          .forEach(view -> opponentsHand.get(index-1).getChildren().add(view));
    }
    status.getTableData().freeTiles.stream()
        .map(tile -> new TileView(tile, TileSource.HAND, 0, 0))
        .forEach(view -> deckView.getChildren().add(view));
    scrollPane.setContent(deckView);
    debugArea.setVisible(debugMode);
  }
}


