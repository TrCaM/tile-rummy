package project.rummy.observers;

import project.rummy.game.GameState;

public interface Observer {
  void update(GameState status);
}
