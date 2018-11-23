package project.rummy.networking;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import project.rummy.entities.Color;
import project.rummy.entities.Tile;
import project.rummy.game.DefaultGameInitializer;
import project.rummy.game.GameState;
import project.rummy.game.GameStore;
import project.rummy.networking.messages.ConnectionData;
import project.rummy.networking.messages.ConnectionMessage;
import project.rummy.networking.messages.GameStateMessage;
import project.rummy.networking.messages.StringMessage;

/**
 * Handler implementation for the object echo client.  It initiates the
 * ping-pong traffic between the object echo client and server by sending the
 * first message to the server.
 */
public class ObjectEchoClientHandler extends ChannelInboundHandlerAdapter {

  private final Tile firstMessage;

  /**
   * Creates a client-side handler.
   */
  public ObjectEchoClientHandler() {
    firstMessage = Tile.createTile(Color.ORANGE, 10);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    // Send the first message if this handler is a client-side handler.
    ConnectionData data = new ConnectionData("The Rock", 0);
    ctx.writeAndFlush(new ConnectionMessage(0, data));
    ctx.writeAndFlush(new StringMessage(0, "Braaaa"));
    ctx.writeAndFlush(new GameStateMessage(0, new GameStore(new DefaultGameInitializer()).initializeGame().generateGameState()));
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    // Echo back the received object to the server.
    System.out.println("Received the state");
    System.out.println(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
    ctx.close();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
