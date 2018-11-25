package project.rummy.control;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class ManualController extends Controller {
  private List<Node> components;
  public ManualController() {
    super();
    components = new ArrayList<>();
  }

  @Override
  public String getControllerType(){
    return "human";
  }

  @Override
  public void playTurn() {
    components.forEach(node -> node.setDisable(false));
  }

  @Override
  public void endTurn() {
    components.forEach(node -> node.setDisable(true));
  }

  @Override
  public void closeInput() {
  }

  public void addControlledNode(Node node) {
    components.add(node);
  }
}
