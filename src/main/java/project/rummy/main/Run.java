package project.rummy.main;


import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import project.rummy.game.GameReader.ReadGameState;

import project.rummy.game.GameState;



import java.io.IOException;


public class Run extends javafx.application.Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage){

    ReadGameState gameState = new ReadGameState();
    GameState state = new GameState();
    try {
      state = gameState.read();
    }
    catch (IOException e) {
      System.out.println("Whoops something went wrong");
    }
    catch (ParseException e) {
      System.out.println("Not working");

    }

    System.out.println(state.getHandsData()[1].tiles.get(0));

 //   System.out.println(gameState.getPlayerLoad().get(0));
//    GUIController gui = new GUIController();
//    gui.StartMenu(primaryStage);
  }


}
