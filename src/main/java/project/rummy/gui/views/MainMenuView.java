package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.Entity;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;

public class MainMenuView extends Pane {
    private GameFXMLLoader loader;
    private Node MainMenuView;

    // This disable buttons once a menu is popped up
    private boolean disableClick;

    @FXML
    private Button start;

    @FXML
    private Button load;

    @FXML
    private Button credits;

    @FXML
    private Button exit;

    public MainMenuView() {
        super();
        this.loader = new GameFXMLLoader("MainMenu");
        loader.setController(this);
        loadMainMenuView();

        this.start.setOnMouseClicked(this::onStartClicked);
        this.load.setOnMouseClicked(this::onLoadClicked);
        this.credits.setOnMouseClicked(this::onCreditsClicked);
        this.exit.setOnMouseClicked(this::onExitClicked);
        this.disableClick = false;
    }

    private void loadMainMenuView() {
        try {
            MainMenuView = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can not load hand");
        }
        getChildren().setAll(MainMenuView);
    }

    private void onStartClicked(MouseEvent mouseEvent) {

        if (!disableClick) {
            disableClick = true;
            Entity selectGameView = EntitiesBuilder.buildGameSelect();
            selectGameView.setX(700);
            selectGameView.setY(400);
            FXGL.getGameWorld().addEntities(selectGameView);
        }
    }

    private void onLoadClicked(MouseEvent mouseEvent) {
        if (!disableClick) {

        }
        System.out.println("Load");
    }

    private void onCreditsClicked(MouseEvent mouseEvent) {
        if (!disableClick) {

        }
        System.out.println("Credits");
    }

    private void onExitClicked(MouseEvent mouseEvent) {
        System.out.println("Exit");
        if (!disableClick) {
            System.exit(0);
        }
    }


}
