package project.rummy.main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.rummy.Observer.TableSource;
import project.rummy.entities.Table;
import project.rummy.gui.GUIController;
import javax.swing.*;


public class Run extends javafx.application.Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception{
      System.out.println("Enter Text: ");
    TableSource tableSource = new TableSource();
    tableSource.addObserver(((o, arg) -> {
      System.out.println("This ran " + arg);
    }));

    new Thread(tableSource).start();

    GUIController gui = new GUIController();
    gui.StartMenu(primaryStage);
  }


}
