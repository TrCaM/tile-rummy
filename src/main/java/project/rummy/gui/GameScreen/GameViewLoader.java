package project.rummy.gui.GameScreen;


import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import project.rummy.gui.GUIController;


import java.awt.*;


public class GameViewLoader extends GUIController{
   /** fxml issues with this one, in this situation I think I would be best not to use this. unless it's the option menu
    public void LoadGameFXML(Stage stage) throws Exception {
        BorderPane pane = new BorderPane();
        TileView tileView = new TileView();
        GridPane pane1 = new GridPane();
        Table table = new Table();
        table.initTiles();

        pane.setPrefSize(getScene_width(), getScene_height());

        Parent loader = FXMLLoader.load(getClass().getResource("/fxml/GameView.fxml"));
    //    pane.getChildren().add(loader);
        pane.setTop(loader);


        tileView.showTile(pane1, table.getFreeTiles().get(0),0);
//        pane.getChildren().add(tileView);
//        pane.setCenter(tileView);

        stage.setScene(new Scene(pane, getScene_width(), getScene_height()));

        stage.show();
    }
**/

    public void LoadGame(Stage stage) throws Exception {
            BorderPane pane = new BorderPane();

        GridPane gridPane = ViewTiles(stage);
        BorderPane.setMargin(gridPane, new Insets(40, 40, 40, 40));
        gridPane.setRotate(90);
        pane.setLeft(gridPane);

        GridPane gridPane1 = ViewTiles(stage);
        BorderPane.setMargin(gridPane1, new Insets(12, 12, 12, 12));
        pane.setBottom(gridPane1);


        stage.setScene(new Scene(pane, getScene_width(), getScene_height()));
        stage.show();

        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SHIFT) {
                    PauseGame(stage);
                }
            }
        });


    }


}