package project.rummy.gui.views;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import project.rummy.entities.Meld;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;

import java.util.stream.IntStream;

public class MeldView extends HBox {

  private Meld meld;

  public MeldView(Meld meld) {
    super();
    this.meld = meld;
    loadMeldView(meld);
  }

  public Meld getMeld() {
    return meld;
  }

  private void loadMeldView(Meld meld) {
    ObservableList<Node> children = getChildren();
    IntStream.range(0, meld.tiles().size())
        .mapToObj(index -> {
          Tile tile = meld.tiles().get(index);
          return new TileView(tile, TileSource.HAND_MELD, meld.getId(), index);
        }).forEach(children::add);
  }
}
