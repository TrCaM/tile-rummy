package project.rummy.control;

public class ManualController extends Controller {
  public ManualController() {
    super();
  }

  @Override
  public String getControllerType(){
    return "human";
  }

  @Override
  public void playTurn() {
  }
}
