package project.rummy.main;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import io.netty.channel.Channel;
import org.json.simple.parser.ParseException;
import project.rummy.commands.CommandProcessor;
import project.rummy.game.*;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.game.GameReader.SaveGame;
import project.rummy.gui.views.EntitiesBuilder;
import project.rummy.messages.StringMessage;
import project.rummy.networks.ClientGameManager;
import project.rummy.networks.GameClientTask;

import java.io.IOException;

import static project.rummy.gui.views.EntityType.GAME;

public class TileRummyApplication extends GameApplication {
  private GameStore gameStore;
  private GameStore startStore;


  private CommandProcessor processor;
  private Game game;
  private GameState state;
  private Channel channel;
  private boolean isConnected;
  private boolean isGameStarted;
  private boolean isStarting;
  private final String PLAYER_NAME = "Tri Tha Thu";
  private ClientGameManager clientGameManager;


  public TileRummyApplication() {
    super();
    gameStore = new GameStore(new StartGameInitializer());
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
//    clientGameManager = new ClientGameManager(this);
//    try {
//      new GameClientTask(PLAYER_NAME, clientGameManager).connectToServer().subscribe(this::setChannel);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
    setUpGame(gameStore.initializeGame());
  }


  public void setUpGame(Game game) {
    this.game = game;
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    this.isStarting = true;
  }

  private void startNetWorkGame() {
    isGameStarted = true;
    int startGamePlayer = 0;
    game.startGame();
    state = GameState.generateState(game);
    state.setCurrentPlayer(startGamePlayer);

    // like the views here works...
    buildGameView();

  }

  private void buildGameView() {
    Entity gameEntity = Entities.builder().type(GAME).build();
    gameEntity.addComponent(game);
    getGameWorld().addEntities(gameEntity);
    Entity handView = EntitiesBuilder.buildHand(game.getControlledPlayer(), state);
    handView.setX(0);
    handView.setY(740);
    Entity tableView = EntitiesBuilder.buildTable(game.getControlledPlayer(), state);
    Entity gameInfoView = EntitiesBuilder.buildGameInfo(game.getControlledPlayer(), state);
    gameInfoView.setX(1150);

    getGameWorld().addEntities(handView, tableView, gameInfoView);
  }

  private void startGame() {
    isGameStarted = true;
//    String fileName="";
//    if(!getParameters().getRaw().isEmpty()) {
//      fileName = getParameters().getRaw().get(0);
//    }
//    ReadGameState gm = new ReadGameState();
//    try {
//      this.state = gm.read(fileName);
//      LoadGameInitializer initializer = new LoadGameInitializer(this.state);
//    }
//    catch (IOException e) {
//      System.out.println("Whoops something went wrong");
//    }
//    catch (ParseException e) {
//      System.out.println("Not working");
//
//    }
//    GameStore gameStore1 = new GameStore(new LoadGameInitializer(state));
//    //game = gameStore1.initializeGame();
//    game = gameStore.initializeGame();
    GameStart start = new GameStart(game);
    game.setStatus(GameStatus.STARTING);
    int startGamePlayer = start.getPlayerValue();
    game.startGame();
    state = GameState.generateState(game);
    state.setCurrentPlayer(startGamePlayer);

    // like the views here works...

    buildGameView();

    Entity startView = EntitiesBuilder.buildGameStart(game, state, startGamePlayer);
    startView.setX(500);
    startView.setY(500);

    getGameWorld().addEntity(startView);


//    SaveGame saveGame = new SaveGame();
//    try {
//      saveGame.save(state);
//    }
//    catch (IOException e) {
//      System.out.println("Whoops something went wrong");
//    }
  }

  @Override
  protected void onUpdate(double tpf) {
    if (isGameStarted) {
      processor.processNext();
      if (game.isGameEnd()) {
        this.getNotificationService().pushNotification(
            String.format("Player %s has won", game.getWinnerName()));
        getGameWorld().clear();
        CommandProcessor.getInstance().reset();
      }
    } else if (isStarting) {
      if (game.isNetworkGame()) {
        startNetWorkGame();
      } else {
        startGame();
      }
    }
  }

  @Override
  protected void exit() {
    super.exit();
    System.exit(0);
  }

  public Channel getChannel() {
    return this.channel;
  }

  private void setChannel(Channel channel) {
    this.channel = channel;
    this.isConnected = true;
  }

  public static void main(String[] args) {
    launch(args);
//    new Thread(() -> launch(args)).start();
//    new Thread(() -> launch(args)).start();
  }
}
