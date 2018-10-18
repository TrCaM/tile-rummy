package project.rummy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.rummy.entities.Table;
import project.rummy.gui.GameScreen.GameViewLoader;
import project.rummy.gui.StartScreen.StartView;
import project.rummy.gui.tile.TileView;

import java.security.Key;
import java.util.Optional;

public class GUIController {
    private Scene scene;
    private int scene_width = 1200;
    private int scene_height = 900;

    public void StartMenu(Stage stage) {
        StartView startView = new StartView();
        GridPane pane = new GridPane();
        startView.LoadStartMenu(pane, stage);
        scene = new Scene(pane, scene_width, scene_height);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    public void OptionMenu(Stage stage) {

    }

    public GridPane ViewTiles(Stage stage) {
        // temperory code
        Table table = new Table();
        table.initTiles();
        table.shuffle();

        TileView tileView = new TileView();
        GridPane gridPane = new GridPane();


        for (int i = 0; i < 13; i++) {
            tileView.showTile(gridPane, table.getFreeTiles().get(i), i);
        }
        Scene scene = new Scene(gridPane, 1200, 900);
        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        stage.setTitle("Press Shift to Pause");
        tileView.moveTile(gridPane, scene);


        return gridPane;

    }

    public void gameView(Stage stage) {
        try {
            GameViewLoader view = new GameViewLoader();
            view.LoadGame(stage);
        }
        catch (Exception e) {
            System.out.println("something is wrong");
        }
    }


    public void returnMainMenu(Stage stage) {
        GUIController guiController = new GUIController();
        guiController.StartMenu(stage);
    }
    public void PauseGame(Stage stage) {
        // get original background, proof of concept
        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    Parent parent = stage.getScene().getRoot();

                    Pane pane = new Pane();

                    Rectangle rectangle = new Rectangle(getScene_width() / 3, getScene_height(), Color.WHITE);

                    // The buttons configured
                    Button start = new Button("Tile Rummy");
                    start.setStyle("-fx-background-color: white; -fx-font-size: 2em; ");

                    Button returnToMainMenu = new Button("Return To Main Menu");
                    returnToMainMenu.setStyle("-fx-background-color: white; -fx-font-size: 2em; ");
                    returnToMainMenu.setLayoutY(100);
                    returnToMainMenu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            returnMainMenu(stage);
                        }
                    });


                    Button exit = new Button("Exit");
                    exit.setStyle("-fx-background-color: White; -fx-font-size: 2em; ");
                    exit.setLayoutY(175);
                    exit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            exitGame();
                        }
                    });

                    pane.getChildren().addAll(rectangle, start, returnToMainMenu, exit);

                    StackPane tempPane = new StackPane();
                    tempPane.getChildren().addAll(parent, pane);
                    stage.setScene(new Scene(tempPane, getScene_width(), getScene_height()));
                    stage.show();

                    stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode() == KeyCode.ESCAPE) {
                                System.out.println("back to menu");
                                unPauseGame(stage, parent);
                            }
                        }
                    });
                }
            }
        });

    }

    public void unPauseGame(Stage stage, Parent parent) {
        // Ok this needs to be changed
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(parent);

        stage.setScene(new Scene(stackPane, getScene_width(), getScene_height()));
        stage.show();

        PauseGame(stage);

    }

    public void exitGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit Game");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you want to quit tile-rummy?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> type = alert.showAndWait();
        if (type.get() == yes) {
            System.exit(0);
        }

    }


    public int getScene_height() {
        return this.scene_height;
    }

    public int getScene_width() {
        return scene_width;
    }
}
