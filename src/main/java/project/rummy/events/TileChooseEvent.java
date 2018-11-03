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
  private int row;
  private int col;

  public static final EventType<TileChooseEvent> TILE_CHOSEN = new EventType(ANY, "TILE_CHOSEN");

  public TileChooseEvent(Tile tile, TileSource tileSource, boolean isChoosing, int row, int col) {
    super(TileChooseEvent.TILE_CHOSEN);
    this.tileSource = tileSource;
    this.tile = tile;
    this.isChoosing = isChoosing;
    this.row = row;
    this.col = col;
    this.parameter = parameter;
  }

  public int getCol() {
    return this.col;
  }

  public int getRow() {
    return this.row;
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
