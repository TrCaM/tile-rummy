package project.rummy.main;

import javafx.fxml.FXMLLoader;

public class GameFXMLLoader extends FXMLLoader {
  public GameFXMLLoader(String resource) {
    super();
    String url = String.format("/fxml/%s.fxml", resource);
    this.setLocation(getClass().getResource(url));
  }
}
