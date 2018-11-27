package project.rummy.main;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import org.json.simple.parser.ParseException;
import project.rummy.commands.CommandProcessor;
import project.rummy.game.*;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.game.GameReader.SaveGame;
import project.rummy.gui.views.EntitiesBuilder;

import java.io.IOException;

import static project.rummy.gui.views.EntityType.GAME;

public class TileRummyApplication extends GameApplication {
  private GameStore gameStore;
  private GameStore startStore;


  private CommandProcessor processor;
  private Game game;
  private GameState state;

  public TileRummyApplication() {
    super();
    gameStore = new GameStore(new DefaultGameInitializer());
    startStore = new GameStore(new StartGameInitializer());
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
    String fileName="";
    if(!getParameters().getRaw().isEmpty()) {
      fileName = getParameters().getRaw().get(0);
    }
    ReadGameState gm = new ReadGameState();
    try {
      this.state = gm.read(fileName);
        LoadGameInitializer initializer = new LoadGameInitializer(this.state);
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {
      System.out.println("Not working");

    }
    GameStore gameStore1 = new GameStore(new LoadGameInitializer(state));
  //  game = gameStore1.initializeGame();
//      game = gameStore.initializeGameStart();

      game = startStore.initializeGameStart();
      GameStart start = new GameStart(game);
      int startGamePlayer = start.getPlayerValue();

    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    Entity gameEntity = Entities.builder().type(GAME).build();
    gameEntity.addComponent(game);
    game.nextTurn();
    state = GameState.generateState(game);
    state.setCurrentPlayer(startGamePlayer);

    // like the views here works...

    getGameWorld().addEntities(gameEntity);
    Entity handView = EntitiesBuilder.buildHand(game.getControlledPlayer(), state);
    handView.setX(0);
    handView.setY(740);
    Entity tableView = EntitiesBuilder.buildTable(game.getControlledPlayer(), state);
    Entity gameInfoView = EntitiesBuilder.buildGameInfo(state);
    gameInfoView.setX(1150);

    Entity startView = EntitiesBuilder.buildGameStart(state, startGamePlayer);
    startView.setX(500);
    startView.setY(500);

    getGameWorld().addEntities(handView, tableView, gameInfoView, startView);



    SaveGame saveGame = new SaveGame();
    try {
      saveGame.save(state);
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
      CommandProcessor.getInstance().reset();
//      exit();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

}
