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
import project.rummy.networks.GameClientTask;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static project.rummy.gui.views.EntityType.GAME;

public class TileRummyApplication extends GameApplication {

  private CommandProcessor processor = null;
  private Game game = null;
  private GameState state = null;
  private Channel channel = null;
  private ClientGameManager clientGameManager = null;

  private static TileRummyApplication INSTANCE;

  public static TileRummyApplication getInstance() {
    return INSTANCE;
  }

  public TileRummyApplication() {
    super();
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

  public void startLoadedGame(String fileName) {
    ReadGameState gm = new ReadGameState();
    try {
      this.state = gm.read(fileName);
    } catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {
      System.out.println("Not working");
    }
    game = new GameFactory(new LoadGameInitializer(state)).initializeGame();
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
    buildGameView();
    game.startGame();
  }

  public void showMainMenu() {
    if (game != null) {
      game.endGame();
    }
    getGameWorld().clear();
    Entity mainMenuView = EntitiesBuilder.buildMainMenu();
    getGameWorld().addEntities(mainMenuView);
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
    showMainMenu();
  }

  public void startNetworkClient() {
    clientGameManager = new ClientGameManager();
    try {
      String PLAYER_NAME = "Tri Tha Thu";
      new GameClientTask(PLAYER_NAME, clientGameManager).connectToServer().subscribe(this::setChannel);
    } catch (Exception e) {
      e.printStackTrace();
    }
    getGameWorld().clear();
  }

  public void startNetworkGame(GameState initialState, int playerId) {
    game = new GameFactory(new NetworkGameInitializer(initialState, playerId, channel, clientGameManager))
        .initializeGame();
    game.setStatus(GameStatus.NETWORK_START);
    game.registerObserver(clientGameManager);
    processor = CommandProcessor.getInstance();
    processor.setUpGame(game);
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

  @Override
  protected void onUpdate(double tpf) {
    if (game != null) {
      switch (game.getStatus()) {
        case NOT_STARTED:
          game.findFirstPlayer();
          buildGameView();
          break;
        case NETWORK_START:
          buildGameView();
          game.setStatus(GameStatus.STARTING);
          break;
        case FINDING_FIRST:
          readyToPlay();
          game.setStatus(GameStatus.WAITING);
          break;
        case STARTING:
          game.setStatus(GameStatus.WAITING);
          game.startGame(true);
          break;
        case TURN_END:
          game.nextTurn();
          game.setStatus(GameStatus.RUNNING);
          processor.reset();
          break;
        case RUNNING:
          processor.processNext();
          if (game.isGameEnd()) {
            this.getNotificationService().pushNotification(
                String.format("%s has won", game.getWinnerName()));
          }
          break;
        case GAME_END:
          CommandProcessor.getInstance().reset();
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
  }

  public static void main(String[] args) {
    launch(args);
  }
}
