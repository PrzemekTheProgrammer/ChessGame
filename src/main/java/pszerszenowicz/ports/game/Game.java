package pszerszenowicz.ports.game;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Game {

    private final Board board;
    private final List<Move> moveHistory = new ArrayList<>();
    protected Set<Move> legalMoves;
    Player actualPlayer;

    protected Game(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Move getLastMove() {
        if (!moveHistory.isEmpty()){
            return moveHistory.get(moveHistory.size()-1);
        }
        return null;
    }

    public void addToHistory(Move move) {
        moveHistory.add(move);
    }
    public void removeLastMoveFromHistory() {
        moveHistory.remove(moveHistory.size()-1);
    }

    public abstract Set<Move> getLegalMoves(Player player);
    public abstract void makeMove(Move move);

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public void startGame() {
        actualPlayer = board.getWhite();
        legalMoves = getLegalMoves(board.getWhite());
    }

}
