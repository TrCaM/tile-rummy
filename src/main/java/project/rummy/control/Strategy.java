package project.rummy.control;

public interface Strategy {
  void iceBreak(ActionHandler handler);
  void performFullTurn(ActionHandler handler);
}
