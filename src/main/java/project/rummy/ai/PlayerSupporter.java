package project.rummy.ai;

import project.rummy.behaviors.ComputerMoveMaker;
import project.rummy.behaviors.PlayAllMeldsMoveMaker;
import project.rummy.control.ActionHandler;
import project.rummy.entities.Meld;
import project.rummy.entities.PlayerStatus;
import project.rummy.entities.Tile;
import project.rummy.game.GameState;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerSupporter {
  private List<Tile> handTiles;
  private List<Meld> tableMelds;
  private GameState state;
  private CombinationSeeker seeker;
  private boolean shouldAnalyze;

  public PlayerSupporter(List<Tile> tiles, List<Meld> melds) {
    this.state = null;
    this.handTiles = tiles;
    this.tableMelds = melds;
    this.shouldAnalyze = false;
    this.seeker = new CombinationSeeker(handTiles, tableMelds);
  }

  public PlayerSupporter(GameState state) {
    this.state = state;
    this.handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;
    this.tableMelds = state.getTableData().melds;
    this.shouldAnalyze = true;
    this.seeker = new CombinationSeeker(handTiles, tableMelds);
  }

  private void clearHints() {
    handTiles.stream().forEach(tile -> tile.setSuggestion(false));
    tableMelds.forEach(meld -> meld.tiles().forEach(tile -> tile.setSuggestion(false)));
  }

  private boolean suggestAddDirectly(Tile tile) {
    clearHints();

    int id = TableMeldSeeker.findDirectMeld(tile.value(), tile.color(), tableMelds);
    if (id != 0) {
      tile.setSuggestion(true);
      Meld.getMeldFromId(id, tableMelds).tiles().stream().forEach(tile1 -> tile1.setSuggestion(true));
      return true;
    }

    return false;
  }

  private boolean suggestManipulationSet(Tile tile) {
    clearHints();

    List<Tile> goodTiles = new ArrayList<>();
    Map<Meld, Integer> map;
    goodTiles.add(tile);

    map = seeker.formSet(goodTiles);
    if (goodTiles.size() + map.size() >= 3) {
      goodTiles.stream().forEach(tile1 -> tile1.setSuggestion(true));
      map.keySet().stream().forEach(meld -> meld.tiles().get(map.get(meld)).setSuggestion(true));
      return true;
    }

    return false;
  }

  private boolean suggestManipulationRun(Tile tile) {
    clearHints();

    Map<Meld, Integer> map = seeker.formRunBySplitRight(tile.value(), tile.color());

    if (!map.isEmpty()) {
      tile.setSuggestion(true);
      for (Meld m : map.keySet()) {
        for (int i = map.get(m)+1; i < m.tiles().size(); i++) {
          m.tiles().get(i).setSuggestion(true);
        }
      }
      return true;
    }

    List<Tile> goodTiles = new ArrayList<>();
    goodTiles.add(tile);

    Map<Meld, Integer> map2 = seeker.formRunByDetaching(goodTiles);

    if (map2.size() + goodTiles.size()>= 3) {
      goodTiles.stream().forEach(tile1 -> tile1.setSuggestion(true));
      map2.keySet().stream().forEach(meld -> meld.tiles().get(map2.get(meld)).setSuggestion(true));
      return true;
    }
    return false;

  }


  private boolean formMeld_hint() {
    clearHints();
    List<Meld> goodMelds = HandMeldSeeker.findBestMelds(handTiles);
    if (goodMelds.isEmpty()) {
      return false;
    }
    goodMelds.get(0).tiles().stream().forEach(tile -> tile.setSuggestion(true));
    return true;

  }


  private boolean manipulate_hint() {
    for (Tile tile : handTiles) {
      clearHints();
      if(!shouldAnalyze){
        if(suggestAddDirectly(tile) || suggestManipulationSet(tile) || suggestManipulationRun(tile)){
          return true;
        }
      } else{
        SmartStateAnalyzer analyzer = new SmartStateAnalyzer(state);
        if (analyzer.shouldPlay(tile) &&
            (suggestAddDirectly(tile) || suggestManipulationSet(tile) || suggestManipulationRun(tile))) {
          return true;
        }
      }
    }
    return false;

  }

  public boolean displayHints_1() {
    return formMeld_hint() ? true : manipulate_hint();
  }


  public boolean displayHints_4() {

    if (state.getPlayerStatuses()[state.getCurrentPlayer()] == PlayerStatus.START) {
      return formMeld_hint();
    }
    List<Tile> handTiles = HandMeldSeeker.findRemainingTiles(state.getHandsData()[state.getCurrentPlayer()].tiles);

    return handTiles.isEmpty() ? formMeld_hint() : manipulate_hint();
  }
}
