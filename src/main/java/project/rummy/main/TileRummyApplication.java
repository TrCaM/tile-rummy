package project.rummy.main;


import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import io.netty.channel.Channel;
import org.json.simple.parser.ParseException;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.PlayerData;
import project.rummy.game.*;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.gui.views.EntitiesBuilder;
import project.rummy.networks.ClientGameManager;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

  private static TileRummyApplication INSTANCE;

  public static TileRummyApplication getInstance() {
    return INSTANCE;
  }


  public TileRummyApplication() {
    super();
    gameStore = new GameStore(new StartGameInitializer());
    INSTANCE = this;
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

  public void startSinglePlayerGame(List<PlayerData> playerDataList) {
    game = new Game(new CustomGameInitializer(playerDataList), false);
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    game.setUpPlayers();
  }

  public void startLoadedGame() {
    String fileName="TestCase1";
    if(!getParameters().getRaw().isEmpty()) {
      fileName = getParameters().getRaw().get(0);
    }
    ReadGameState gm = new ReadGameState();
    try {
      this.state = gm.read(fileName);
      LoadGameInitializer initializer = new LoadGameInitializer(this.state);
    } catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {
      System.out.println("Not working");

    }
    game = new GameStore(new LoadGameInitializer(state)).initializeGame();
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    buildGameView();
    game.startGame();
//    game = gameStore.initializeGame();
  }

  private void readyToPlay() {
    Timer timer = new Timer();
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            game.setStatus(GameStatus.STARTING);
          }
        },
        3000
    );
  }

  @Override
  protected void initGame() {
//    clientGameManager = new ClientGameManager(this);
//    try {
//      new GameClientTask(PLAYER_NAME, clientGameManager).connectToServer().subscribe(this::setChannel);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    setUpGame(gameStore.initializeGame());
    Entity mainMenuView = EntitiesBuilder.buildMainMenu();
    getGameWorld().addEntities(mainMenuView);
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
    getGameWorld().clear();
    state = game.generateGameState();
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

  private void startSinglePlayerGame() {
    isGameStarted = true;
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
    if (game != null) {
      switch (game.getStatus()) {
        case NOT_STARTED:
          game.findFirstPlayer();
          buildGameView();
          break;
        case FINDING_FIRST:
          readyToPlay();
          game.setStatus(GameStatus.WAITING);
          break;
        case STARTING:
          game.setStatus(GameStatus.WAITING);
          game.startGame(true);
          break;
        case RUNNING:
          processor.processNext();
          if (game.isGameEnd()) {
            this.getNotificationService().pushNotification(
                String.format("Player %s has won", game.getWinnerName()));
            getGameWorld().clear();
            CommandProcessor.getInstance().reset();
          }
          break;
      }
    }
////    switch (game.getStatus()) {
////      //TODO
////    }
//    if (isGameStarted) {
//    } else if (isStarting) {
//      if (game.isNetworkGame()) {
//        startNetWorkGame();
//      } else {
//        startNetWorkGame();
//      }
//    }
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
