package project.rummy.main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import project.rummy.entities.Table;
import project.rummy.gui.tile.TileView;

import java.awt.*;


public class Run extends javafx.application.Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Table table = new Table();
    table.initTiles();
    table.shuffle();

    /** This displays the tiles now, Yay**/
    TileView tileView = new TileView();
    HBox box = new HBox();
    GridPane gridPane = new GridPane();

    /** Looking at Tiles**/
    for (int i = 0; i < 14; i++) {
      tileView.showTile(box, table.getFreeTiles().get(i));
    }
    gridPane.add(box,0, 0);

    Scene scene = new Scene(gridPane, 1200, 900);
    gridPane.setAlignment(Pos.BOTTOM_CENTER);
    primaryStage.setTitle("Tiles Shown");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
