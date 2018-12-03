package project.rummy.gui.views;

import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;

import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.game.GameState;

public class EntitiesBuilder {
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

  public static Entity buildMainMenu() {
    return Entities.builder()
        .type(EntityType.MainMenu)
        .viewFromNode(new MainMenuView())
        .build();
  }
}
