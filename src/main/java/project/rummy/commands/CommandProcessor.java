package project.rummy.commands;

import project.rummy.control.ActionHandler;
import project.rummy.game.Game;
import project.rummy.game.GameState;

import java.util.LinkedList;
import java.util.Queue;

public class CommandProcessor {
  private Game game;
  private ActionHandler handler;
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
  }

  public void setUpGame(Game game) {
    this.game = game;
  }

  public void setUpHandler(ActionHandler handler) {
    this.handler = handler;
  }

  /**
   * Execute the command. Throw if the action handler used is expired. This is because we want every
   * handler to be used only in one turn.
   */
  public void apply(Command command) {
    if (game == null) {
      throw new IllegalStateException("Game was not set up properly");
    }
    if (handler == null || handler.isExpired()) {
      throw new IllegalStateException("ActionHandler was not set up properly before the turn");
    }

    command.execute(handler);

    game.update(handler.getTurnStatus());
  }

  public void processNext() {
    if (!commands.isEmpty()) {
      apply(commands.remove());
    } else if (!chunks.isEmpty()) {
      applyChunk(chunks.remove());
    }
  }

  private void applyChunk(CommandChunks chunks) {
    GameState gameState = GameState.generateState(game);
    commands.addAll(chunks.execute(gameState, handler));
    proccessAllCommands();
  }

  public void proccessAllCommands() {
    while (!commands.isEmpty()) {
      apply(commands.remove());
    }
    while (!chunks.isEmpty()) {
      applyChunk(chunks.remove());
    }
  }

  public void enqueueCommand(Command command) {
    commands.add(command);
  }

  public void enqueueChunks(CommandChunks chunk) {
    chunks.add(chunk);
  }

  public void reset() {
    commands.clear();
    chunks.clear();
  }
}
