package project.rummy.gui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;

public class GameInfoView extends Pane {
  private GameFXMLLoader loader;

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

  public GameInfoView(GameState gameState) {
    super();
    this.loader = new GameFXMLLoader("gameInfo");
    loader.setController(this);
    loadGameInfoView(gameState);
  }

  private void loadGameInfoView(GameState gameState) {
    Node gameStateView;
    try {
      gameStateView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load table");
    }
    renderGameInfo(gameState);
    getChildren().setAll(gameStateView);
  }

  private void renderGameInfo(GameState gameState) {
    // Update the num tiles
    oppo1Tiles.setText("" + gameState.getHandsData()[1].tiles.size() + " Tiles");
    oppo2Tiles.setText("" + gameState.getHandsData()[2].tiles.size() + " Tiles");
    oppo3Tiles.setText("" + gameState.getHandsData()[3].tiles.size() + " Tiles");
    // Update current player
    Label currentLabel;
    System.out.println(you.getStyleClass());
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

}


