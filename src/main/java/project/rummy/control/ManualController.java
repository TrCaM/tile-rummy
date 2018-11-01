package project.rummy.control;


import project.rummy.commands.CommandProcessor;

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
