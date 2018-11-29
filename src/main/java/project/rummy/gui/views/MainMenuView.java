package project.rummy.gui.views;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import project.rummy.main.GameFXMLLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MainMenuView extends Pane {
  private GameFXMLLoader loader;

  // This disable buttons once a menu is popped up
  private boolean disableClick;

  @FXML
  private Button start;
  @FXML
  private Button load;
  @FXML
  private Button credits;
  @FXML
  private Button exit;
  @FXML
  private Pane startMenu;
  @FXML
  private Pane gameSetupMenu;
  @FXML
  private Pane offlineButton;
  @FXML
  private Pane onlineButton;
  @FXML
  private ToggleGroup players;
  @FXML
  private ToggleGroup players1;
  @FXML
  private ToggleGroup players2;
  @FXML
  private ToggleGroup players3;
  @FXML
  private Button playButton;

  private Map<String, Pane> menus;
  private List<ToggleGroup> groups;

  MainMenuView() {
    super();
    this.loader = new GameFXMLLoader("MainMenu");
    loader.setController(this);
    loadMainMenuView();

    this.start.setOnMouseClicked(this::onStartClicked);
    this.load.setOnMouseClicked(this::onLoadClicked);
    this.credits.setOnMouseClicked(this::onCreditsClicked);
    this.exit.setOnMouseClicked(this::onExitClicked);
    this.offlineButton.setOnMouseClicked(event -> this.openMenu("setup"));
    this.disableClick = false;
    menus = new HashMap<>();
    groups = Arrays.asList(players, players1, players2, players3);
    setup();
  }

  private void setup() {
    menus.put("start", startMenu);
    menus.put("setup", gameSetupMenu);

    groups.forEach(group -> group.selectedToggleProperty().addListener(this::onGameOptionChanged));
  }

  private void loadMainMenuView() {
    Node mainMenuView;
    try {
      mainMenuView = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("Can not load hand");
    }
    getChildren().setAll(mainMenuView);
  }

  private void onStartClicked(MouseEvent mouseEvent) {
    openMenu("start");
  }

  private void openMenu(String type) {
    menus.values().forEach(pane -> pane.setVisible(false));
    if (menus.containsKey(type)) {
      menus.get(type).setVisible(true);
    }
  }

  private void onLoadClicked(MouseEvent mouseEvent) {
    openMenu("load");
    System.out.println("Load");
  }

  private void onCreditsClicked(MouseEvent mouseEvent) {
    openMenu("credits");
    System.out.println("Credits");
  }

  private void onExitClicked(MouseEvent mouseEvent) {
    System.exit(0);
  }

  private void onGameOptionChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
    long humanCounts =
        groups.stream().filter(group -> group.getToggles().indexOf(group.getSelectedToggle()) == 0).count();
    long disableCounts =
        groups.stream().filter(group -> group.getToggles().indexOf(group.getSelectedToggle()) == 4).count();
    playButton.setDisable(humanCounts > 1 || disableCounts > 2);
  }
}
