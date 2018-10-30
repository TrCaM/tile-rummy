package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import project.rummy.entities.HandData;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;

public class EntitiesBuilder {

  public static Entity buildTile(Tile tile) {
    Entity tileEntity = Entities.builder()
        .type(EntityType.TILE)
        .viewFromNode(new TileView(tile))
        .build();
    tileEntity.addComponent(tile);
    return tileEntity;
  }

  public static Entity buildMeld(Meld meld) {
    Entity meldEntity = Entities.builder()
        .type(EntityType.MELD)
        .viewFromNode(new MeldView(meld))
        .build();
    meldEntity.addComponent(meld);
    return meldEntity;
  }

  public static Entity buildHand(HandData handData) {
    Entity handEntity = Entities.builder()
        .type(EntityType.MELD)
        .viewFromNode(new HandView(handData))
        .build();
    handEntity.addComponent(handData);
    return handEntity;
  }

}
