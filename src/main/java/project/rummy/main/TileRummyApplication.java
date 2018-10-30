package project.rummy.main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.settings.GameSettings;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;
import project.rummy.game.Game;
import project.rummy.gui.views.EntitiesBuilder;


public class TileRummyApplication extends GameApplication {

  public TileRummyApplication() {
    super();
  }
  @Override
  protected void initSettings(GameSettings settings) {
    settings.setWidth(1900);
    settings.setHeight(1000);
    settings.setTitle("Tile Rummy");
    settings.setVersion("0.1");
  }

  @Override
  protected void initGame() {
    Entity tileView = EntitiesBuilder.buildTile(new Tile(Color.ORANGE, 4));
    tileView.setX(300);
    tileView.setY(600);
    getGameWorld().addEntity(tileView);
  }

  public static void main(String[] args) {
    launch(args);
  }


}
