package project.rummy.main;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.view.ScrollingBackgroundView;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import org.json.simple.parser.ParseException;
import project.rummy.entities.*;
import project.rummy.game.GameReader.ReadGameState;
import project.rummy.game.GameReader.WriteGameState;
import project.rummy.game.GameState;
import project.rummy.gui.views.EntitiesBuilder;

import java.io.IOException;
import java.util.Arrays;

public class TileRummyApplication extends GameApplication {

  public TileRummyApplication() {
    super();
  }

  @Override
  protected void initUI() {
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
    System.out.println(Color.RED.value());
    Entity tileView = EntitiesBuilder.buildTile(new Tile(Color.ORANGE, 4));
    tileView.setX(300);
    tileView.setY(600);
    Entity tileView2 = EntitiesBuilder.buildTile(new Tile(Color.RED, 13));
    tileView2.setX(300);
    tileView2.setY(800);
    Tile meldTile1 = new Tile(Color.ORANGE, 6);
    Tile meldTile2 = new Tile(Color.ORANGE, 7);
    Tile meldTile3 = new Tile(Color.ORANGE, 8);
    Tile B9 = new Tile(Color.BLACK, 9);
    Tile R9 = new Tile(Color.RED, 9);
    Tile G9 = new Tile(Color.GREEN, 9);
    Entity meldView = EntitiesBuilder.buildMeld(Meld.createMeld(meldTile1, meldTile2, meldTile3));
    Entity meldView2 = EntitiesBuilder.buildMeld(Meld.createMeld(B9, R9, G9));
    meldView2.setY(200);
    Tile O5 = Tile.createTile(Color.ORANGE, 5);
    Tile O6 = Tile.createTile(Color.ORANGE, 6);
    Tile O7 = Tile.createTile(Color.ORANGE, 7);
    Tile O8 = Tile.createTile(Color.ORANGE, 8);
    Tile O9 = Tile.createTile(Color.ORANGE, 9);
    Tile R3 = Tile.createTile(Color.RED, 3);
    Tile G3 = Tile.createTile(Color.GREEN, 3);
    Tile B3 = Tile.createTile(Color.BLACK, 3);
    Tile O3 = Tile.createTile(Color.ORANGE, 3);
    Tile R13 = Tile.createTile(Color.RED, 13);
    Tile R12 = Tile.createTile(Color.RED, 12);
    Tile R11 = Tile.createTile(Color.RED, 11);
    Hand hand = new Hand(Arrays.asList(O5, O6, O7, O8, O9, R3, G3, B3, O3));
    hand.formMeld(0, 1, 2);
    hand.formMeld(0, 1);
    Entity handView = EntitiesBuilder.buildHand(hand.toHandData());
    handView.setX(0);
    handView.setY(740);
    Table table = new Table();
    table.addMeld(Meld.createMeld(O5, O6, O7, O8, O9));
    table.addMeld(Meld.createMeld(meldTile1, meldTile2, meldTile3));
    table.addMeld(Meld.createMeld(G3, R3, B3, O3));
    table.addMeld(Meld.createMeld(G3, R3, B3, O3));
    table.addMeld(Meld.createMeld(R9, G9, B9));
    table.addMeld(Meld.createMeld(R13, R12, R11));
    Entity tableView = EntitiesBuilder.buildTable(table.toTableData());
    getGameWorld().addEntities(handView, tableView);
  }

  public static void main(String[] args) {
    launch(args);
  }


}
