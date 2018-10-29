package project.rummy.gui.tile;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.rummy.entities.Tile;
import project.rummy.gui.StartScreen.StartView;

import java.awt.*;

/**
 * This class represent view for a Tile
 */
public class TileView extends Pane{
  private int width = 50;
  private int height = 60;


  private Color getColor(Tile tile) {
    if (tile.color().toString().equals(project.rummy.entities.Color.BLACK.toString())) {
      return Color.BLACK;
    }

    if (tile.color().toString().equals(project.rummy.entities.Color.RED.toString())) {
      return Color.RED;
    }

    if (tile.color().toString().equals(project.rummy.entities.Color.GREEN.toString())) {
      return Color.GREEN;
    }

    if (tile.color().toString().equals(project.rummy.entities.Color.ORANGE.toString())) {
      return Color.ORANGE;
    }

    return null;
  }
  public void showTile(GridPane pane, Tile tile, int location) {
    // configure the rectangle

    Rectangle viewTile = new Rectangle(width, height, getColor(tile));
    Text value = new Text();
    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(viewTile , value);
    value.setText(Integer.toString(tile.value()));
    value.setFill(Color.WHITE);

    pane.add(stackPane, location, 0);
  }



  public void moveTile(GridPane gridPane, Scene scene) {
    /** this moves a single tile **/
    for (Node node: gridPane.getChildren()) {
        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            double x_translate = node.getTranslateX()+ event.getX() - width/2;
            double y_translate = node.getTranslateY() + event.getY() - height/2;

            // check for tile overlap
            if (intersects(gridPane, node, event)) {
                System.out.println(true);
            }
            else {
                node.setTranslateX(x_translate);
                node.setTranslateY(y_translate);
            }

        }
      });
    }

  }

    public boolean intersects(GridPane gridPane, Node node, MouseEvent event) {
        Bounds curNodeBound = node.localToScene(node.getBoundsInLocal());
        Bounds compNodeBound;

        for (Node compNode: gridPane.getChildren()) {
            compNodeBound = compNode.localToScene(compNode.getBoundsInLocal());
            if (compNodeBound.intersects(curNodeBound)) {
                if (compNodeBound.getMinY() < curNodeBound.getMinY() ) {
    //                node.setTranslateY(node.getTranslateX() + event.getX() - width/2);
    //                return true;
                }
                if (compNodeBound.getMaxX() > curNodeBound.getMinX()) {
    //                node.setTranslateY(compNodeBound.getMaxX() - event.getX());
//                    return true;
                }
                /**
                if (compNodeBound.getMinY() > curNodeBound.getMaxY()) {
                    node.setTranslateY(compNodeBound.getMinY() - event.getY());
                    return true;
                }
                if (compNodeBound.getMaxY() < curNodeBound.getMinY()){
                    node.setTranslateY(compNodeBound.getMaxX() - event.getY());
                    return true;
                } **/
            }
        }
        return false;
    }
}
