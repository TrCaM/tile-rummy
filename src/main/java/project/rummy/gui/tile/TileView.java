package project.rummy.gui.tile;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.rummy.entities.Tile;

/**
 * This class represent view for a Tile
 */
public class TileView extends Pane{
  private int width = 50;
  private int height = 60;
  private int posX;
  private int posY;
  private Color color;


  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

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
  public void showTile(HBox box, Tile tile) {
    // configure the rectangle
    Rectangle viewTile = new Rectangle(width, height, getColor(tile));
    Text value = new Text();
    StackPane stackPane = new StackPane();

    stackPane.getChildren().addAll(viewTile , value);
    value.setText(Integer.toString(tile.value()));
    value.setFill(Color.WHITE);
    box.getChildren().add(stackPane);
  }


}
