package project.rummy.game.GameReader;

public enum PlayerLoadTypes {
    HUMAN ("HUMAN"),
    PLAYER1("Player 1"),
    PLAYER2("Player 2"),
    Player3 ("Player 3");

    private String name;

    PlayerLoadTypes(String name){
        this.name = name;
    }
    @Override
    public java.lang.String toString() {
        return this.name;
    }
}
