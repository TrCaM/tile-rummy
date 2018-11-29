package project.rummy.main;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.settings.GameSettings;
import project.rummy.commands.CommandProcessor;
import project.rummy.game.Game;
import project.rummy.game.GameState;
import project.rummy.gui.views.EntitiesBuilder;

public class TileRummyMenu extends GameApplication {
    private GameState game;

    @Override
    protected void initSettings(GameSettings settings) {

        settings.setWidth(1900);
        settings.setHeight(1000);
        settings.setTitle("Tile Rummy Menu");
        settings.setVersion("0.1");
    }
    protected void initGame() {
        Entity mainMenuView = EntitiesBuilder.buildMainMenu();
        getGameWorld().addEntities(mainMenuView);
    }

    @Override
    protected void onUpdate(double tpf) {

    }

}
