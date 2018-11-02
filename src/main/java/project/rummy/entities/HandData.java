package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.Arrays;
import java.util.List;

public class HandData extends Component {
  public List<Tile> tiles;
  public List<Meld> melds;

  HandData(Hand hand) {
    this.tiles = hand.getTiles();
    this.melds = hand.getMelds();
  }

  public static Hand toHand(HandData data) {
    Hand hand = new Hand();
    data.tiles.forEach(hand::addTile);
    data.melds.forEach(hand::addMeld);
    return hand;
  }
}

