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
        // there are 4 types of controllers {"human", "strategy1", "strategy2", "strategy3"}

        Controller controller;
        Player[] players = new Player[4];
        Hand hand;

        for(int i=0; i<4; i++){

            //System.out.println(state.getHandsData()[i].tiles.toString());

            hand = new Hand(state.getHandsData()[i].tiles, state.getHandsData()[i].melds);

            if(state.getPlayerData()[i].controllerType.equals("human")){
                controller = new ManualController();
            }else if(state.getPlayerData()[i].controllerType.equals("strategy1")){
                controller = new AutoController(new Strategy1(game));
            }else if(state.getPlayerData()[i].controllerType.equals("strategy2")) {
                controller = new AutoController(new Strategy2(game));
            }else{
                controller = new AutoController(new Strategy3(game));
            }

            players[i] = new Player(state.getPlayerData()[i].name, controller, hand, state.getPlayerStatuses()[i]);
        }


        game.setUpPlayer(players);
        game.setTurnNumber(state.getTurnNumber());
    }

    @Override
    public void initTable(Game game) {
        TableData data = state.getTableData();
        Table table = new Table(data.melds, data.freeTiles, data.setGrid1, data.setGrid2, data.runGrid);
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