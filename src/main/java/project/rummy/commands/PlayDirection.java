package project.rummy.commands;

import java.util.ArrayList;
import java.util.List;

public class PlayDirection {
    private List<Command> commands;
    private List<CommandChunks> chunks;

    public PlayDirection() {
        commands = new ArrayList<>();
        chunks = new ArrayList<>();
    }

    public PlayDirection(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
        chunks = new ArrayList<>();
    }

    public PlayDirection(List<Command> commands, List<CommandChunks> chunks) {
        this.commands = new ArrayList<>(commands);
        this.chunks = new ArrayList<>(chunks);
    }

    public void addDirection(Command command) {
        this.commands.add(command);
    }

    public void addDirection(CommandChunks chunk) {
        this.chunks.add(chunk);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public List<CommandChunks> getChunks() {
        return chunks;
    }
}
