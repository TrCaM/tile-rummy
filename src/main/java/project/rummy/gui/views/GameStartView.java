package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.settings.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.TurnStatus;
import project.rummy.game.*;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;

import static project.rummy.gui.views.EntityType.GAME;


public class GameStartView extends Pane implements Observer {
    private GameFXMLLoader loader;
    private Node StartView;
    private int playerNum;
    private GameState state;

    @FXML
    private Text playerStart;
    @FXML
    private Button ok;


    public GameStartView(GameState state, int playerStart) {
        super();
        this.loader = new GameFXMLLoader("gameStart");
        loader.setController(this);
        loadGameStartView(state);
        this.state = state;
        this.playerNum = playerStart;

        playerText(state, playerStart);
        this.ok.setOnMouseClicked(this::onOkClicked);
        // I get this is causing lag
        Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
        game.registerObserver(this);
    }

    private void playerText(GameState state, int player) {
        playerStart.setText("Player " + player + " starts");
    }

    private void onOkClicked(MouseEvent mouseEvent) {
        // this should load a new game
        GameStore store = new GameStore(new DefaultGameInitializer());

        Game game = store.initializeGame();
        CommandProcessor processor;
        FXGL.getGameWorld().clear();

        processor = CommandProcessor.getInstance();
        processor.setUpGame(game);
        Entity gameEntity = Entities.builder().type(GAME).build();
        gameEntity.addComponent(game);
        game.setCurrentPlayer(playerNum);
        game.startGame();
        FXGL.getGameWorld().addEntities(gameEntity);
 //
        game.registerObserver(this);
        state = GameState.generateState(game);


        Entity handView = EntitiesBuilder.buildHand(game.getControlledPlayer(), state);
        handView.setX(0);
        handView.setY(740);
        Entity tableView = EntitiesBuilder.buildTable(game.getControlledPlayer(), state);
        Entity gameInfoView = EntitiesBuilder.buildGameInfo(game.getControlledPlayer(), state);
        gameInfoView.setX(1150);

        FXGL.getGameWorld().addEntities(handView, tableView, gameInfoView);
    }

    private void loadGameStartView(GameState gameState) {
        Node gameStateView;
        try {
            gameStateView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can not load table");
        }
        update(gameState);
        getChildren().setAll(gameStateView);
    }

    public void update(GameState status) {
        status = this.state;
    }


}
