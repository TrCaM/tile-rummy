package project.rummy.observers;

public interface Observable {
  void registerObserver(Observer observer);
  void notifyObservers();
}
