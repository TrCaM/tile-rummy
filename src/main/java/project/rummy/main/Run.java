package project.rummy.main;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.rummy.entities.Table;
import project.rummy.gui.tile.TileView;

import java.awt.*;


public class Run extends javafx.application.Application {
  private double startDragX;
  private double startDragY;
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
    GridPane gridPane = new GridPane();
    Button button = new Button("Reset Hand");

    /** Looking at Tiles**/
    for (int i = 0; i < 14; i++) {
      tileView.showTile(gridPane, table.getFreeTiles().get(i), i);
    }

    Scene scene = new Scene(gridPane, 1200, 900);
    gridPane.setAlignment(Pos.BOTTOM_CENTER);
    primaryStage.setTitle("Tiles Shown DEBUG");
    tileView.moveTile(gridPane);

    primaryStage.setScene(scene);
    primaryStage.show();
  }


}
