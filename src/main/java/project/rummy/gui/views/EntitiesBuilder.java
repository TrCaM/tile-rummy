package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import project.rummy.entities.*;
import project.rummy.game.GameState;

import java.util.List;

public class EntitiesBuilder {

//  public static Entity buildTile(Tile tile) {
//    Entity tileEntity = Entities.builder()
//        .type(EntityType.TILE)
//        .viewFromNode(new TileView(tile))
//        .build();
//    tileEntity.addComponent(tile);
//    return tileEntity;
//  }

  public static Entity buildMeld(Meld meld) {
    Entity meldEntity = Entities.builder()
        .type(EntityType.MELD)
        .viewFromNode(new MeldView(meld))
        .build();
    meldEntity.addComponent(meld);
    return meldEntity;
  }

  public static Entity buildHand(Player controlledPlayer, GameState gameState) {
    Entity handEntity = Entities.builder()
        .type(EntityType.HAND)
        .viewFromNode(new HandView(controlledPlayer, gameState))
        .build();
    handEntity.addComponent(gameState.getHandsData()[0]);
    return handEntity;
  }

  public static Entity buildTable(Player controlledPlayer, GameState gameState) {
    Entity tableEntity = Entities.builder()
        .type(EntityType.TABLE)
        .viewFromNode(new TableView(controlledPlayer, gameState))
        .build();
    tableEntity.addComponent(gameState.getTableData());
    return tableEntity;
  }

  public static Entity buildGameInfo(Player controlledPlayer, GameState gameState) {
    Entity tableEntity = Entities.builder()
        .type(EntityType.GAME_INFO)
        .viewFromNode(new GameInfoView(controlledPlayer, gameState))
        .build();
    tableEntity.addComponent(gameState);
    return tableEntity;
  }
}
