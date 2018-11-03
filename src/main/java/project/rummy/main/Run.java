package project.rummy.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;
import project.rummy.entities.Table;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.Game;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.game.GameReader.WriteGameState;
import project.rummy.game.GameState;
import project.rummy.gui.GUIController;
import javax.swing.*;
import java.io.IOException;


public class Run extends javafx.application.Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage){
  /**
    GameState game = new GameState();
    WriteGameState testState = new WriteGameState(game);
    try {

      testState.write();
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    } **/

    ReadGameState gameState = new ReadGameState();
    try {
      gameState.read();
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {

    }

    System.out.println(gameState.getCurrentPlayer());
    System.out.println(gameState.getPlayerLoad().get(0).getName());
    System.out.println(gameState.getDeck());
//    GUIController gui = new GUIController();
//    gui.StartMenu(primaryStage);
  }


}
