package project.rummy.control;

import com.almasb.fxgl.app.FXGL;
import org.apache.log4j.Logger;
import project.rummy.entities.*;
import project.rummy.game.Game;
import project.rummy.gui.views.EntityType;

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
  private boolean canEnd;
  private HandData backUpHand;
  private TableData backUpTable;
  private String playerName;
  private int startPoint;
  private PlayerStatus turnType;

  private static Logger logger = Logger.getLogger(ActionHandler.class);

  public ActionHandler(Player player, Table table) {
    this.hand = player.hand();
    this.canUseTable = player.status() != START;
    this.table = table;
    this.manipulationTable = ManipulationTable.getInstance();
    manipulationTable.clear();
    this.startPoint = hand.getScore();
    this.isTurnEnd = false;
    this.playerName = player.getName();
    this.canDraw = true;
    this.canPlay = true;
    this.canEnd = false;
    this.turnType = player.status();
  }

  public Hand getHand(){
    return this.hand;
  }

  public TurnStatus getTurnStatus() {
    TurnStatus status = new TurnStatus();
    status.canDraw = canDraw;
    status.canPlay = canPlay;
    status.isTurnEnd = isTurnEnd;
    status.canEnd = canEnd;
    return status;
  }

  public void backUpTurn() {
    this.backUpHand = hand.toHandData();
    this.backUpTable = table.toTableData();
  }

  public void restoreTurn() {
    Game game = FXGL.getGameWorld().getEntitiesByType(EntityType.GAME).get(0).getComponent(Game.class);
    this.hand = HandData.toHand(backUpHand);
    this.table = TableData.toTable(backUpTable);
    game.setUpTable(table);
    game.getCurrentPlayerObject().setHand(hand);
    this.manipulationTable.clear();
    this.isTurnEnd = false;
    this.canDraw = true;
    this.canPlay = true;
    this.canEnd = false;
  }

  public void formMeld(int ...indexes){
    hand.formMeld(indexes);
  }

  public boolean isExpired() {
    return this.isTurnEnd;
  }

  public void draw() {
    Tile tile = table.drawTile();
    tile.setHightlight(true);
    hand.addTile(tile);
    hand.sort();
    this.canDraw = false;
    this.canPlay = false;
    this.canEnd = true;
    logger.info(String.format("%s has draw %s", playerName, tile));
  }

  public void playFromHand(int meldIndex) {
    //TODO: Add a logging infomation here
    if (meldIndex >=0 && meldIndex < hand.getMelds().size()) {
      manipulationTable.add(hand.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }

  public void playFromHand(Meld meld) {
    //TODO: Add a logging infomation here
    playFromHand(hand.getMelds().indexOf(meld));
  }

  public void takeTableMeld(int meldIndex){
    //TODO: Add a logging infomation here
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (meldIndex >=0 && meldIndex < table.getPlayingMelds().size()) {
      manipulationTable.add(table.removeMeld(meldIndex));
    } else {
      throw new IllegalArgumentException("Invalid meld index");
    }
  }
  public void takeHandTile(int tileIndex){
    //TODO: Add a logging infomation here
    if (!canUseTable) {
      throw new IllegalStateException("Cannot manipulate table");
    }
    if (tileIndex >=0 && tileIndex < hand.getTiles().size()) {
      manipulationTable.add(Meld.createMeld(hand.removeTile(tileIndex)));
    } else {
      throw new IllegalArgumentException("Invalid tile index");
    }
  }

  public void endTurn() {
    // TODO: Add logging information here
    isTurnEnd = true;
  }

  public void submit() {
    this.canDraw = false;
    manipulationTable.submit(table);
    manipulationTable.clear();
    if (turnType == ICE_BROKEN  || startPoint - hand.getScore() >= 30) {
      this.canEnd = true;
    }
  }

  public ManipulationTable getManipulationTable() {
    return this.manipulationTable;
  }
}
