package project.rummy.commands;

import project.rummy.control.ActionHandler;
import project.rummy.game.Game;
import project.rummy.game.GameState;

import java.util.*;

public class CommandProcessor {
  private Game game;
  private ActionHandler handler;
  private List<ActionHandler> handlers;
  private List<Queue<Command>> queues;
  private Queue<Command> commands;
  private Queue<CommandChunks> chunks;

  private static final CommandProcessor INSTANCE = new CommandProcessor();

  public static CommandProcessor getInstance() {
    return INSTANCE;
  }


  private CommandProcessor() {
    commands = new LinkedList<>();
    chunks = new LinkedList<>();
    game = null;
    handler = null;
    handlers = new ArrayList<>();
    queues = new ArrayList<>();
    for (int i = 0; i< 4; i++) {
      queues.add(new LinkedList<>());
    }
  }

  /**
   * Set up the {@link Game} for processing the commands.
   */
  public void setUpGame(Game game) {
    this.game = game;
  }

  /**
   * Set up the {@link ActionHandler} for processing the commands.
   */
  public void setUpHandler(ActionHandler handler) {
    this.handler = handler;
  }

  public void setUpHandlers(List<ActionHandler> handlers) {
    this.handlers.addAll(handlers);
  }
  /**
   * Execute the command. Throw if the action handler used is expired. This is because we want every
   * handler to be used only in one turn.
   */
  public void apply(Command command, ActionHandler handler) {
    if (game == null) {
      throw new IllegalStateException("Game was not set up properly");
    }
    if (handler == null || handler.isExpired()) {
      throw new IllegalStateException("ActionHandler was not set up properly before the turn");
    }
    command.execute(handler);
    game.update(handler.getTurnStatus(), handlers.indexOf(handler));
  }

  public void processNext() {
    for (int i = 0; i < 4; i++) {
      if (!queues.get(i).isEmpty()) {
        apply(queues.get(i).remove(), handlers.get(i));
      } else if (!chunks.isEmpty()) {
        applyChunk(chunks.remove());
      }
    }
  }

  private void applyChunk(CommandChunks chunks) {
    GameState gameState = GameState.generateState(game);
    commands.addAll(chunks.execute(gameState, handler));
  }

  public void enqueueCommand(Command command, int playerId) {
    queues.get(playerId).add(command);
  }

  public void enqueueChunks(CommandChunks chunk) {
    chunks.add(chunk);
  }

  public void reset() {
    commands.clear();
    chunks.clear();
    queues.forEach(Collection::clear);
  }
}
