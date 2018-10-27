package project.rummy.game;

import project.rummy.entities.PlayerStatus;
import project.rummy.entities.Meld;

import java.util.List;

public class GameStatus {
  private int turnNumber;
  private PlayerStatus[] status;
  private List<Meld> tableMelds;
}
