package project.rummy.gui.StartScreen;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.rummy.gui.GUIController;


public class StartView {
    private GUIController gui = new GUIController();


    public void LoadStartMenu(GridPane pane, Stage stage) {
       int location = 0;

        Text gameName = new Text("Name Name");
        gameName.setStyle("-fx-background-color: transparent; -fx-font-size: 3em;" );
        pane.add(gameName, 0, location++);


        Button start = new Button("Start");
        start.setStyle("-fx-background-color: #00ff00; -fx-font-size: 3em; ");
        pane.add(start, 0, location++);

        start.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
           getNewGame(stage);
         }
        });

        Button load = new Button("Load");
        load.setStyle("-fx-background-color: white; -fx-font-size: 3em; ");
        pane.add(load, 0, location++);

        load.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {

         }
        });
        Button options = new Button("Option");
        options.setStyle("-fx-background-color: #00ff00; -fx-font-size: 3em; ");
        pane.add(options, 0, location++);

       options.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

        }
        });
        Button quit = new Button("Quit");
        quit.setStyle("-fx-background-color: #00ff00; -fx-font-size: 3em; ");
        pane.add(quit, 0, location);
        pane.setTranslateX(50);

        quit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
         gui.exitGame();
      }
     });
    }

    public void getNewGame(Stage stage) {
     // temperory game start
        /**
        gui.ViewTiles(stage);
        gui.returnMainMenu(stage); **/

        gui.gameView(stage);
    }


}
