package project.rummy.game;

import io.netty.channel.Channel;
import project.rummy.control.Controller;
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
      hand = new Hand(getState().getHandsData()[i].tiles, getState().getHandsData()[i].melds);

      if(i == playerId){
        controller = new ManualNetworkController(channel, gameManager);
      } else {
        controller = new NetworkController(channel, gameManager, i);
      }

      players[i] = new Player(getState().getPlayerData()[i].name, controller, hand, getState().getPlayerStatuses()[i], i);
    }

    game.setUpPlayers(players);
    game.setTurnNumber(getState().getTurnNumber());
    game.setControlledPlayer(playerId);
  }
}
