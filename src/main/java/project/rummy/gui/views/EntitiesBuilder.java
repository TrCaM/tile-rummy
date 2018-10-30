package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.shape.Rectangle;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;

public class EntitiesBuilder {

  public static Entity buildTile(Tile tile) {
    return Entities.builder()
        .viewFromNode(new TileView(new Tile(Color.ORANGE, 4)))
        .build();
  }
}
