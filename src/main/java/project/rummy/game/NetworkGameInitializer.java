package project.rummy.game;

import io.netty.channel.Channel;
import project.rummy.control.Controller;
import project.rummy.control.ManualController;
import project.rummy.control.ManualNetworkController;
import project.rummy.control.NetworkController;
import project.rummy.entities.Hand;
import project.rummy.entities.Player;
import project.rummy.networks.ClientGameManager;


public class NetworkGameInitializer extends LoadGameInitializer {
  private ClientGameManager gameManager;
  private int playerId;
  private Channel channel;

  public NetworkGameInitializer(GameState state, int playerId, Channel channel, ClientGameManager gameManager) {
    super(state);
    this.playerId = playerId;
    this.channel = channel;
    this.gameManager = gameManager;
  }

  @Override
  public void initPlayers(Game game) {
    Player[] players = new Player[4];
    Hand hand;
    for(int i=0; i<4; i++){
      Controller controller;
      hand = new Hand(state.getHandsData()[i].tiles, state.getHandsData()[i].melds);

      if(i == playerId){
        controller = new ManualNetworkController(channel, gameManager);
      } else {
        controller = new NetworkController(channel, gameManager);
      }

      players[i] = new Player(state.getPlayerData()[i].name, controller, hand, state.getPlayerStatuses()[i], i);
    }

    game.setUpPlayer(players);
    game.setTurnNumber(state.getTurnNumber());
    game.setControlledPlayer(playerId);
  }
}
