package project.rummy.events;

import javafx.event.Event;
import javafx.event.EventType;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;

public class TileUnChooseEvent extends Event {
  private String parameter;

  private Tile tile;
  private TileSource tileSource;

  public static final EventType<TileUnChooseEvent> TILE_UNCHOSEN = new EventType(ANY, "TILE_UNCHOSEN");

  public TileUnChooseEvent(Tile tile, TileSource tileSource) {
    super(TileUnChooseEvent.TILE_UNCHOSEN);
    this.tileSource = tileSource;
    this.tile = tile;
    this.parameter = parameter;
  }

  public String getParameter() {
    return this.parameter;
  }

  public Tile getTile() {
    return tile;
  }

  public TileSource getTileSource() {
    return tileSource;
  }

  public TileUnChooseEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
