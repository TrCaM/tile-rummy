package project.rummy.main;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.view.ScrollingBackgroundView;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;

import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.*;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;

import project.rummy.gui.views.EntitiesBuilder;

import java.io.IOException;
import java.util.Arrays;

import static project.rummy.entities.PlayerStatus.ICE_BROKEN;
import static project.rummy.entities.PlayerStatus.START;
import static project.rummy.gui.views.EntityType.GAME;

public class TileRummyApplication extends GameApplication {
  private GameStore gameStore;
  private CommandProcessor processor;
  private Game game;

  private Entity handView;
  private Entity tableView;
  private Entity gameInfoView;
  private Entity gameEntity;

  public TileRummyApplication() {
    super();
    gameStore = new GameStore(new DefaultGameInitializer());
  }

  @Override
  protected void initUI() {
  }

  @Override
  protected void initSettings(GameSettings settings) {
    settings.setWidth(1900);
    settings.setHeight(1000);
    settings.setTitle("Tile Rummy");
    settings.setVersion("0.1");



  }

  @Override
  protected void initGame() {
    game = gameStore.initializeGame();
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    gameEntity = Entities.builder().type(GAME).build();
    gameEntity.addComponent(game);
    game.nextTurn();
    GameState gameState = GameState.generateState(game);
    getGameWorld().addEntities(gameEntity);
    handView = EntitiesBuilder.buildHand(gameState);
    handView.setX(0);
    handView.setY(740);
    tableView = EntitiesBuilder.buildTable(gameState);
    gameInfoView = EntitiesBuilder.buildGameInfo(gameState);
    gameInfoView.setX(1300);
    getGameWorld().addEntities(handView, tableView, gameInfoView);
  }

  @Override
  protected void onUpdate(double tpf) {
    processor.processNextCommand();
  }

  public static void main(String[] args) {
    launch(args);
  }


}
