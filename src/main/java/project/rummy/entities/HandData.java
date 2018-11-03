package project.rummy.entities;

import com.almasb.fxgl.entity.component.Component;

import java.util.List;

public class HandData extends Component {
  public List<Tile> tiles;
  public List<Meld> melds;

  public HandData(Hand hand) {
    this.tiles = hand.getTiles();
    this.melds = hand.getMelds();
  }
}

