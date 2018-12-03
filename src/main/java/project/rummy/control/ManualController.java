package project.rummy.control;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class ManualController extends Controller {
  public ManualController() {
    super();
  }

  @Override
  public String getControllerType() {
    return "human";
  }

  @Override
  public void playTurn() {
  }

  @Override
  public void endTurn() {
  }

  @Override
  public void closeInput() {
  }
}
