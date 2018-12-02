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

import java.util.ArrayList;
import java.util.List;

public class NetworkGameManager {
  private ChannelGroup channels;
  private final int MAX_PLAYERS = 4;
  private List<PlayerInfo> playersInfo;
  private Game game;
  private GameState gameState;
  private boolean isGameRunning;

  public static NetworkGameManager INSTANCE;

  public static NetworkGameManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new NetworkGameManager();
    }
    return INSTANCE;
  }

  private NetworkGameManager() {
    channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    playersInfo = new ArrayList<>(4);
    isGameRunning = false;
  }

  public void onChannelConnected(Channel channel) {
    if(channels.size() < MAX_PLAYERS) {
      initChannel(channel);
    } else {
      channel.writeAndFlush(new StringMessage("The lobby is full")).addListener(ChannelFutureListener.CLOSE);
    }
  }

  public void onChannelDisconnect(Channel channel) {
    System.out.println("Channel Disconnected");
    for (PlayerInfo info : playersInfo) {
      if (channel.id().equals(info.getChannelId())) {
        playersInfo.remove(info); 
      }
    }
    if (isGameRunning) {
      notifyGameEnd();
    } else {
      notifyPlayerConnected();
    }
  }

  private void notifyGameEnd() {
    channels.close().addListener(future -> resetServer());
  }

  private void resetServer() {
    playersInfo.clear();
    game = null;
    gameState = null;
    isGameRunning = false;
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
    playersInfo.add(new PlayerInfo(data.getChannelId(), data));
    channel
        .writeAndFlush(playersInfo.get(playersInfo.size()-1).getApprovedMessage())
        .addListener(channelFuture -> notifyPlayerConnected())
        .addListener(channelFuture -> tryStartGame());
  }

  private void tryStartGame() {
    if (playersInfo.size() == MAX_PLAYERS) {
      game = new GameStore(new ServerGameInitializer(toPlayerInfoArray(playersInfo))).initializeGame();
      game.setStatus(GameStatus.STARTING);
      game.setCurrentPlayer(-1);
      GameState gameState = game.generateGameState();
      channels
          .writeAndFlush(new GameStateMessage(gameState))
          .addListener(future -> isGameRunning = true);
    }
  }
  
  private PlayerInfo[] toPlayerInfoArray(List<PlayerInfo> infos) {
    return playersInfo.toArray(new PlayerInfo[0]);
  }

  private void notifyPlayerConnected() {
    channels.writeAndFlush(new LobbyMessage(toPlayerInfoArray(playersInfo)));
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
