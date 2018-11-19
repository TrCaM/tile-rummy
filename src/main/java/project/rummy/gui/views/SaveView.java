package project.rummy.gui.views;

import com.almasb.fxgl.app.FXGL;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import project.rummy.game.Game;
import project.rummy.game.GameReader.SaveGame;
import project.rummy.game.GameState;
import project.rummy.main.GameFXMLLoader;
import project.rummy.observers.Observer;

import java.io.IOException;

// This implements the save function as a button.

public class SaveView extends Pane implements Observer {
    private GameFXMLLoader loader;
    private Node saveView;
    private GameState state;

    @FXML
    private Button saveButton;


    public SaveView(GameState state) {
        super();
        this.loader = new GameFXMLLoader("save");
        loader.setController(this);
        loadSaveView(state);

        setId("save");
        this.saveButton.setOnMouseClicked(this::onSaveClicked);
        Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
        game.registerObserver(this);
    }

    public void loadSaveView(GameState state) {
        try {
            saveView = loader.load();
        }
        catch (IOException e) {
            System.out.println("Can not load save");
        }
        update(state);
        getChildren().setAll(saveView);
    }

    private void onSaveClicked(MouseEvent event)
    {
        SaveGame saveGame = new SaveGame();
        try {
            saveGame.save(state);
            System.out.println("Game Saved");
        }
        catch (IOException e) {
            System.out.println("Save is Unsuccessful");
        }
    }
    @Override
    public void update(GameState state) {
        this.state = state;

    }
}

