package project.rummy.entities;

import java.io.Serializable;

public class TurnStatus implements Serializable {
  public boolean canEnd;
  public boolean canDraw;
  public boolean canPlay;
  public boolean isTurnEnd;
  public boolean isIceBroken;
  public boolean goNextTurn;
  public boolean tryEndTurn;
}
