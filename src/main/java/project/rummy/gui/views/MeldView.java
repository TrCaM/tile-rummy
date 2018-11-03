package project.rummy.gui.views;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import project.rummy.entities.Meld;
import project.rummy.entities.TileSource;

public class MeldView extends HBox {

  public MeldView(Meld meld) {
    super();
    loadMeldView(meld);
  }

  private void loadMeldView(Meld meld) {
    ObservableList<Node> children = getChildren();
    meld.tiles().stream()
        .map(tile -> new TileView(tile, TileSource.HAND_MELD, 0, 0))
        .forEach(children::add);
  }
}
