package project.rummy.networks;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import project.rummy.game.*;
import project.rummy.messages.*;

public class NetworkGameManager {
  private ChannelGroup channels;
  private final int MAX_PLAYERS = 2;
  private PlayerInfo[] playersInfo;
  private int nextPlayer;
  private Game game;
  private GameState gameState;

  public static NetworkGameManager INSTANCE;

  public static NetworkGameManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NetworkGameManager();
    }
    return INSTANCE;
  }

  private NetworkGameManager() {
    channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    playersInfo = new PlayerInfo[4];
    nextPlayer = 0;
  }

  public void onChannelConnected(Channel channel) {
    if(channels.size() < MAX_PLAYERS) {
      initChannel(channel);
    } else {
      channel.writeAndFlush(new StringMessage("The lobby is full")).addListener(ChannelFutureListener.CLOSE);
    }
  }

  public void initChannel(Channel channel) {
    ChannelPipeline pipeline = channel.pipeline();
    pipeline.addLast(
        new ObjectEncoder(),
        new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
        new MessageFilterHandler(),
        new MessageHandler(new ServerProcessor()));
  }

  public void onConnectionDataReceived(Channel channel, ConnectionData data) {
    channels.add(channel);
    data.setPlayerId(nextPlayer);
    playersInfo[nextPlayer] = new PlayerInfo(channel.id(), data);
    channel
        .writeAndFlush(playersInfo[nextPlayer++].getApprovedMessage())
        .addListener(channelFuture -> notifyPlayerConnected())
        .addListener(channelFuture -> tryStartGame());
  }

  private void tryStartGame() {
    if (nextPlayer == MAX_PLAYERS) {
      game = new GameStore(new ServerGameInitializer(playersInfo)).initializeGame();
      game.setStatus(GameStatus.STARTING);
      GameState gameState = game.generateGameState();
      channels.writeAndFlush(new GameStateMessage(gameState));
    }
  }

  private void notifyPlayerConnected() {
    channels.writeAndFlush(new LobbyMessage(playersInfo));
  }

  public void goNextTurn() {
    gameState.setGameStatus(GameStatus.TURN_END);
    channels.writeAndFlush(new GameStateMessage(gameState));
  }

  public void onGameUpdate(ChannelId channelId, GameState state) {
    if (state.getGameStatus() != GameStatus.RUNNING && state.getGameStatus() != GameStatus.TURN_END) {
      throw new IllegalStateException("The game should running now");
    }
//    if (state.getCurrentPlayer() != 0) {
//      System.out.println("Need go next");
//      state.setCurrentPlayer(state.getCurrentPlayer() + 1);
//    }
    this.gameState = state;
    if (state.getGameStatus() == GameStatus.TURN_END) {
      channels.writeAndFlush(new GameStateMessage(state));
    } else {
      channels.stream()
          .filter(channel -> !channel.id().equals(channelId))
          .forEach(channel -> channel.writeAndFlush(new GameStateMessage(state)));
    }

  }
}
