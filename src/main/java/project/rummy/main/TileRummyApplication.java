package project.rummy.main;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;

import org.json.simple.parser.ParseException;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.*;
import project.rummy.game.*;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.game.GameReader.WriteGameState;

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
  private GameState state;

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
    ReadGameState gm = new ReadGameState();
    try {
      this.state = gm.read();
      //  LoadGameInitializer initializer = new LoadGameInitializer(this.state);
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {
      System.out.println("Not working");

    }
    GameStore gameStore1 = new GameStore(new LoadGameInitializer(state));
    game = gameStore1.initializeGame();

    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    gameEntity = Entities.builder().type(GAME).build();
    gameEntity.addComponent(game);
    game.nextTurn();
    //state = temp;
    state = GameState.generateState(game);

    // like the views here works...
    getGameWorld().addEntities(gameEntity);
    handView = EntitiesBuilder.buildHand(state);
    handView.setX(0);
    handView.setY(740);
    tableView = EntitiesBuilder.buildTable(state);
    gameInfoView = EntitiesBuilder.buildGameInfo(state);
    gameInfoView.setX(1150);
    getGameWorld().addEntities(handView, tableView, gameInfoView);


    WriteGameState writeGameState = new WriteGameState(state);
    try {
      writeGameState.write();
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }

  }

  @Override
  protected void onUpdate(double tpf) {
    processor.processNext();
    if (game.isGameEnd()) {
      this.getNotificationService().pushNotification(
          String.format("Player %s has won", game.getWinnerName()));
      getGameWorld().clear();
//      exit();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }


}
