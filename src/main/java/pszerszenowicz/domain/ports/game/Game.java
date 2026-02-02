package pszerszenowicz.domain.ports.game;

public interface Game {

    void makeMove(Move move, Player player);
    void initGame();
}
