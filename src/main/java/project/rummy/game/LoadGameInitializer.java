
package project.rummy.game;

import project.rummy.control.AutoController;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.entities.Hand;
import project.rummy.entities.Player;
import project.rummy.entities.Table;
import project.rummy.entities.TableData;

import project.rummy.strategies.Strategy1;
import project.rummy.strategies.Strategy2;
import project.rummy.strategies.Strategy3;
import project.rummy.strategies.Strategy4;

/**
 * Initialize game from the predefined game state. Useful for testing
 */
public class LoadGameInitializer implements GameInitializer {
    private GameState state;

    public LoadGameInitializer(GameState state) {
        this.state = state;
    }

    @Override
    public void initPlayers(Game game) {
        // there are 4 types of controllers {"human", "strategy1", "strategy2", "strategy3", "strategy4"}

        Controller controller;
        Player[] players = new Player[4];
        Hand hand;
        for(int i=0; i<4; i++){
            hand = new Hand(state.getHandsData()[i].tiles, state.getHandsData()[i].melds);

            if("human".equals(state.getPlayerData()[i].controllerType)){
                controller = new ManualController();
            }else if("strategy1".equals(state.getPlayerData()[i].controllerType)){
                controller = new AutoController(game, new Strategy1());
            }else if("strategy2".equals(state.getPlayerData()[i].controllerType)) {
                controller = new AutoController(game, new Strategy2());
            }else if ("strategy3".equals(state.getPlayerData()[i].controllerType)){
                controller = new AutoController(game, new Strategy3());
            }else  {
                controller = new AutoController(game, new Strategy4());
            }

            players[i] = new Player(state.getPlayerData()[i].name, controller, hand, state.getPlayerStatuses()[i], i);
        }


        game.setUpPlayer(players);
        game.setTurnNumber(state.getTurnNumber());
    }

    @Override
    public void initTable(Game game) {
        TableData data = state.getTableData();
        Table table = new Table(data.melds, data.freeTiles, data.setGrid1, data.setGrid2, data.runGrid);

//        for (int i = 0; i < state.getTableData().melds.size(); i++) {
//           table.addMeld(state.getTableData().melds.get(i));
//
//        }

        // then per player plays a meld
        game.setUpTable(table);
    }

    @Override
    public void initEmptyTable(Game game) {
        Table table = new Table();
        game.setUpTable(table);
    }

    @Override
    public void initializeGameState(Player[] players, Table table) {
        /* TODO: We can actually left this empty if you already do everything else before */
        //throw new UnsupportedOperationException();
    }
}