package project.rummy.game;

import com.almasb.fxgl.entity.component.Component;
import project.rummy.commands.CommandProcessor;
import project.rummy.control.ActionHandler;
import project.rummy.entities.Hand;
import project.rummy.entities.Table;
import project.rummy.entities.Player;
import project.rummy.entities.TurnStatus;
import project.rummy.observers.Observable;
import project.rummy.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class Game extends Component implements Observable {
    private Player[] players;
    private Table table;
    private int currentPlayer;
    private List<Observer> observers;
    private int turnNumber;
    private CommandProcessor commandProcessor;
    TurnStatus turnStatus;

    Game() {
        super();
        this.observers = new ArrayList<>();
        this.commandProcessor = CommandProcessor.getInstance();
    }

    public void setUpPlayer(Player[] players) {
        this.players = players;
        this.turnNumber = 0;
        this.currentPlayer = -1;
    }

    public void setUpTable(Table table) {
        this.table = table;
    }

    public Player getCurrentPlayerObject() {
        return players[currentPlayer];
    }

    /**
     * Start the game specifically:
     * + It create the game loops to keep looping until the game is done
     * + Each loop will represent a single move that requires the interface update
     * + Redraw the components that need to be re-rendered after each iteration
     * + Constantly check the state of the game and check for when the game should end
     */
    public void nextTurn() {
        currentPlayer = (currentPlayer + 1) % 4;
        ActionHandler handler = new ActionHandler(players[currentPlayer], table);
        commandProcessor.setUpHandler(handler);
        this.turnStatus = handler.getTurnStatus();
        handler.backUpTurn();
//        players[currentPlayer].getController().playTurn();
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void restoreTurn(Hand hand, Table table) {
        players[currentPlayer].setHand(hand);
        this.table = table;
    }

    /**
     * Check the current state of the game to see that if the game has ended:
     * + When a player has win the game (no tile in hand)
     * + When there's no free tile to draw.
     *
     * @return the player index who won the game. That player either is the only player who has no
     * tile or has the least points in hand when there's no tile to draw and play.
     * Return -1 if the game is not ended.
     */
    public int getWinner() {
        if (players[currentPlayer].hand().size() == 0) {
            return currentPlayer;
        } else if (players[currentPlayer].hand().size() != 0 && table.getFreeTiles().isEmpty()) {

            int leastPoint = players[0].hand().getScore();
            int winner = 0;

            for (int i = 0; i < players.length; i++) {
                if (players[i].hand().getScore() < leastPoint) {
                    leastPoint = players[i].hand().getScore();
                    winner = i;
                }
            }
            return winner;
        } else {
            return -1;
        }
    }

    public boolean isGameEnd() {
        return getWinner() != -1;
    }

    Table getTable() {
        return this.table;
    }

    Player[] getPlayers() {
        return players;
    }

    int getCurrentPlayer() {
        return currentPlayer;
    }

    int getTurnNumber() {
        return turnNumber;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        GameState state = generateGameState();
        observers.forEach(observer -> observer.update(state));
    }


  public void setTurnStatus(TurnStatus turnStatus) {
    this.turnStatus = turnStatus;
  }

  private GameState generateGameState() {
        return GameState.generateState(this);
    }
}
