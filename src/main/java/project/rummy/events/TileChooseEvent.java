package project.rummy.events;

import javafx.event.Event;
import javafx.event.EventType;
import project.rummy.entities.Tile;
import project.rummy.entities.TileSource;

public class TileChooseEvent extends Event {
  private String parameter;

  private Tile tile;
  private TileSource tileSource;
  private boolean isChoosing;

  public static final EventType<TileChooseEvent> TILE_CHOSEN = new EventType(ANY, "TILE_CHOSEN");

  public TileChooseEvent(Tile tile, TileSource tileSource, boolean isChoosing) {
    super(TileChooseEvent.TILE_CHOSEN);
    this.tileSource = tileSource;
    this.tile = tile;
    this.isChoosing = isChoosing;
    this.parameter = parameter;
  }

  public boolean isChoosing() {
    return isChoosing;
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

  public TileChooseEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
