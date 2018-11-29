package project.rummy.control;

import com.almasb.fxgl.app.FXGL;
import org.apache.log4j.Logger;
import project.rummy.commands.CommandProcessor;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.gui.views.EntityType;

import java.util.Collection;
import java.util.List;

import static project.rummy.entities.PlayerStatus.ICE_BROKEN;
import static project.rummy.entities.PlayerStatus.START;

/**
 * This class handles all controllers's interaction with the game.
 */
public class ActionHandler {
  private Hand hand;
  private boolean canUseTable;
  private Table table;
  private ManipulationTable manipulationTable;
  private boolean isTurnEnd;
  private boolean canDraw;
  private boolean canPlay;
  private boolean isIceBroken;
  private HandData backUpHand;
  private TableData backUpTable;
  private String playerName;
  private int startPoint;
  private int tableStartPoint;
  private PlayerStatus turnType;
  private boolean preventUpdate;
  private boolean tryEndTurn;
  private boolean canEndTurn;

  private static Logger logger = Logger.getLogger(ActionHandler.class);
  private boolean goNextTurn;

  public ActionHandler(Player player, Table table) {
    this.hand = player.hand();
    this.canUseTable = player.status() != START;
    this.table = table;
    this.manipulationTable = ManipulationTable.getInstance();
    manipulationTable.clear();
    this.startPoint = hand.getScore();
    this.tableStartPoint = table.getPlayingMelds().stream().mapToInt(Meld::getScore).sum();
    this.isTurnEnd = false;
    this.goNextTurn = false;
    this.playerName = player.getName();
    this.canDraw = true;
    this.canPlay = true;
    this.turnType = player.status();
    this.isIceBroken = player.status() == ICE_BROKEN;
    this.preventUpdate = false;
    this.tryEndTurn = false;
    this.canEndTurn = false;
  }

  public Hand getHand() {
    return this.hand;
  }

  public TurnStatus getTurnStatus() {
    TurnStatus status = new TurnStatus();
    status.canDraw = canDraw;
    status.canPlay = canPlay;
    status.isTurnEnd = isTurnEnd;
    status.isIceBroken = isIceBroken;
    status.canEnd = canEndTurn();
    status.goNextTurn = goNextTurn;
    status.tryEndTurn = tryEndTurn;
    return status;
  }

  public void preventUpdate() {
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.preventUpdate();
  }

  public void enableUpdate() {
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.enableUpdate();
  }

  public void forceUpdate(){
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    game.update(getTurnStatus());
  }
  public void backUpTurn() {
    this.backUpHand = hand.toHandData();
    this.backUpTable = table.toTableData();
//    manipulationTable.clear();
  }

  public void restoreTurn() {
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    this.hand = HandData.toHand(backUpHand);
    this.table = TableData.toTable(backUpTable);
    game.setUpTable(table);
    game.getCurrentPlayerObject().setHand(hand);
    game.getCurrentPlayerObject().resetForNewTurn();
    table.resetForNewTurn();
    this.manipulationTable.clear();
    this.isTurnEnd = false;
    this.canDraw = true;
    this.canPlay = true;
  }

  public void formMeld(int... indexes) {
    if (indexes.length == 0) {
      return;
    }
    hand.formMeld(indexes);
  }

  public void formOneMeld(int... indexes) {
    if (indexes.length == 0) {
      return;
    }
    hand.formOneMeld(indexes);
  }

  private boolean canEndTurn() {
    if (canEndTurn) {
      return true;
    }
    int newTableScore = table.getPlayingMelds().stream().mapToInt(Meld::getScore).sum();

    if (turnType == START) {
      return (newTableScore-tableStartPoint >= 30 || hand.getScore() > startPoint)
          && manipulationTable.isEmpty();
    }
    return hand.getScore() != startPoint && manipulationTable.isEmpty();
  }

  public boolean isExpired() {
    return this.goNextTurn;
  }

  public void drawAndEndTurn() {
    draw();
    endTurn();
  }

  public void draw() {
    Tile tile = table.drawTile();
    tile.setHightlight(true);
    hand.addTile(tile);
    hand.sort();
    this.canDraw = false;
    this.canPlay = false;
    System.out.println(String.format("%s has drawAndEndTurn %s", playerName, tile));
  }

  /**
   * The player drawAndEndTurn 3 cards on penaties
   */
  public void draw(int times) {
    for (int i = 0 ; i < times; i++) {
      draw();
    }
  }

  public void timeOutEndTurn() {
    if (canEndTurn()) {
      endTurn();
    } else if (manipulationTable.getMelds().isEmpty()) {
      drawAndEndTurn();
    } else {
      // Penalty if player leaves a invalid state
      restoreTurn();
      draw(3);
      endTurn();
    }
  }

  public void drawOrEndTurn() {
    if (canEndTurn()) {
      endTurn();
    } else {
      drawAndEndTurn();
    }
  }

  public void playFromHand(int meldIndex) {
    //TODO: Add a logging infomation here
    if (meldIndex >= 0 && meldIndex < hand.getMelds().size()) {
      manipulationTable.add(hand.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }

  public void playFromHand(Meld meld) {
    //TODO: Add a logging infomation here
    playFromHand(hand.getMelds().indexOf(meld));
  }

  public List<Meld> playAllMeldFromHand() {
    manipulationTable.getMelds().addAll(hand.getMelds());
    return hand.clearMeld();
  }

  public void takeTableMelds(Collection<Meld> melds) {
    melds.forEach(meld -> takeTableMeld(table.getPlayingMelds().indexOf(meld)));
  }

  public void takeTableMeld(int meldIndex) {
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (meldIndex >= 0 && meldIndex < table.getPlayingMelds().size()) {
      manipulationTable.add(table.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }
  public void takeTableMeld(Meld m) {
    takeTableMeld(table.toTableData().melds.indexOf(m));
  }

  public void updateFromData(TableData tableData, HandData data, TurnStatus status) {
    table.update(tableData);
    hand.update(data);
    if (status != null) {
      isIceBroken = status.isIceBroken;
      isTurnEnd = status.isTurnEnd;
      goNextTurn = status.goNextTurn;
      canDraw = status.canDraw;
      canPlay = status.canPlay;
      canEndTurn = status.canEnd;
    }
  }

  public void takeHandTile(int tileIndex) {
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (tileIndex >= 0 && tileIndex < hand.getTiles().size()) {
      manipulationTable.add(Meld.createMeld(hand.removeTile(tileIndex)));
    } else {
      throw new IllegalArgumentException("Invalid tile index");
    }
  }

  public void takeHandTile(Tile t) {
    int index = hand.getTiles().indexOf(t);
    takeHandTile(index);
  }


  public void stash() {
    CommandProcessor processor = CommandProcessor.getInstance();
    if (!canEndTurn()) {
      throw new IllegalStateException("Cannot stash if you cannot end");
    }
    processor.reset();
  }

  public void endTurn() {
    if (!canEndTurn()) {
      throw new IllegalStateException("Can not end the turn now");
    }
    isTurnEnd = true;
  }

  public void tryEndTurn() {
    if (!canEndTurn()) {
      throw new IllegalStateException("Can not end the turn now");
    }
    tryEndTurn = true;
    goNextTurn = false;
  }

  public void nextTurn(boolean doCheck) {
    if (doCheck || isTurnEnd) {
      endTurn();
    }
    tryEndTurn = false;
    goNextTurn = true;
  }

  public void nextTurn() {
    nextTurn(true);
  }

  public void submit() {
    this.canDraw = false;
    manipulationTable.submit(table);
    manipulationTable.clear();
//    if (turnType == ICE_BROKEN || startPoint - hand.getScore() >= 30) {
//      this.isIceBroken = true;
//    }
    int newTableScore = table.getPlayingMelds().stream().mapToInt(Meld::getScore).sum();
    if (turnType == ICE_BROKEN || newTableScore - tableStartPoint >= 30) {
      this.isIceBroken = true;
    }
  }

  public void submit(Meld meld) {
    manipulationTable.submit(meld, table);
    this.canDraw = false;
//    if (turnType == ICE_BROKEN || startPoint - hand.getScore() >= 30) {
//      this.isIceBroken = true;
//    }
    int newTableScore = table.getPlayingMelds().stream().mapToInt(Meld::getScore).sum();
    if (turnType == ICE_BROKEN || newTableScore - tableStartPoint >= 30) {
      this.isIceBroken = true;
    }
  }

  public ManipulationTable getManipulationTable() {
    return this.manipulationTable;
  }
}
