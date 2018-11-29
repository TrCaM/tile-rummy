package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import project.rummy.commands.CommandProcessor;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.game.GameStatus;
import project.rummy.main.GameFXMLLoader;
import project.rummy.main.TileRummyApplication;

import java.io.IOException;

import static project.rummy.gui.views.EntityType.GAME;

public class GameTypeStart extends Pane {
    private GameFXMLLoader loader;
    private Node gameTypeSelectionView;

    @FXML
    private Button singlePlayer;

    @FXML
    private Button multi_player;


    @FXML
    private Button back;

    public GameTypeStart () {
        super();
        this.loader = new GameFXMLLoader("GameType");
        loader.setController(this);
        loadGameSelectView();
        this.singlePlayer.setOnMouseClicked(this::onSinglePlayClicked);
        this.back.setOnMouseClicked(this::onBackClicked);

    }


    private void loadGameSelectView() {
        try {
            gameTypeSelectionView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can not load Selection View");
        }
        getChildren().setAll(gameTypeSelectionView);
    }

    private void onSinglePlayClicked(MouseEvent mouseEvent) {
        //loaad single player

    }

    private void onBackClicked(MouseEvent mouseEvent) {
        Entity mainMenu = EntitiesBuilder.buildMainMenu();
        FXGL.getGameWorld().clear();
        FXGL.getGameWorld().addEntities(mainMenu);
        System.out.println("Back");
    }


}
