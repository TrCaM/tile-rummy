package project.rummy.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        Parent parent = stage.getScene().getRoot();

        Pane pane = new Pane();

        Rectangle rectangle = new Rectangle(this.scene_width/3, this.scene_height, Color.WHITE);

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
                    System.exit(0);
            }
        });

        pane.getChildren().addAll(rectangle, start, returnToMainMenu, exit);

        StackPane tempPane = new StackPane();
        tempPane.getChildren().addAll(parent ,pane);


        stage.setScene(new Scene(tempPane, getScene_width(), getScene_height()));
        stage.show();


        
    }



    public int getScene_height() {
        return this.scene_height;
    }

    public int getScene_width() {
        return scene_width;
    }
}
