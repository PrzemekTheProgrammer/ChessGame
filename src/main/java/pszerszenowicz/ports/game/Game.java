package pszerszenowicz.ports.game;

import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {

    private Board board;
    private List<Move> moveHistory = new ArrayList<>();

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

}
