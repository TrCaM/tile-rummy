package project.rummy.gui.GameScreen;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import project.rummy.gui.GUIController;

public class GameBoardView {
    private GUIController guiController;

    public void drawTileGrid(Stage stage) {
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(true);
        stage.setScene(new Scene(pane, guiController.getScene_width(), guiController.getScene_height()));
        stage.show();
    }

}
