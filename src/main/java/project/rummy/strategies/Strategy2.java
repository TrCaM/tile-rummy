package project.rummy.strategies;

import project.rummy.ai.HandMeldSeeker;
import project.rummy.behaviors.*;
import project.rummy.commands.Command;
import project.rummy.commands.PlayDirection;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy 2 as the specifiation.
 */
public class Strategy2 implements Strategy, Observer {
  private GameState state;

  public Strategy2(Game game) {
    game.registerObserver(this);
  }

//  @Override
//  public PlayDirection iceBreak() {
//    ComputerMoveMaker move = new SlowIceBreakingMoveMaker();
//    return new PlayDirection(move.calculateMove(state));
//  }

//  @Override
//  public PlayDirection performFullTurn() {
////    List<Command> commands = new ArrayList<>();
////    List<Command> recievedCmd;
////
////    boolean mustDraw = true;
////
////    ComputerMoveMaker playTile = new FastTilesOnlyMoveMaker();
////
////    recievedCmd = playTile.calculateMove(state);
////    while(!recievedCmd.isEmpty()){
////      mustDraw = false;
////      commands.addAll(recievedCmd);
////      recievedCmd = playTile.calculateMove(state);
////    }
////
////    //find list of tiles that cannot form melds
////    List<Tile> handTiles = state.getHandsData()[state.getCurrentPlayer()].tiles;
////    List<Meld> allMelds = HandMeldSeeker.findBestMelds(handTiles);
////    List<Tile> singleTiles = handTiles;
////    allMelds.stream().forEach(meld -> meld.tiles().forEach(singleTiles::remove));
////
////    //play all the melds when there's no unformed tiles left
////    if(singleTiles.isEmpty() && !allMelds.isEmpty()){
////      for (Meld m : allMelds) {
////        List<Integer> indexes = new ArrayList<>();
////        for (int i = 0; i < m.tiles().size(); i++) {
////          indexes.add(i);
////        }
////        commands.add(handler -> handler.formMeld(indexes.stream().mapToInt(Integer::intValue).toArray()));
////        commands.add(handler -> handler.playFromHand(0));
////      }
////    }
////
////    if(mustDraw){
////      commands.add(handler -> handler.draw());
////      return commands;
////    }
//
//    //TODO send endturn command
//    return null;
//  }

  @Override
  public void update(GameState state) {
    this.state = state;
  }

  @Override
  public PlayDirection iceBreak(GameState gameState) {
    return null;
  }

  @Override
  public PlayDirection performFullTurn(GameState gameState) {
    return null;
  }
}

