package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.exception.MoveNotAvailableException;
import pszerszenowicz.domain.ports.Game;
import pszerszenowicz.domain.ports.Move;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChessGame implements Game {

    private final ChessBoard board = new ChessBoard();
    private final List<ChessMove> moveHistory = new ArrayList<>();
    private Set<ChessMove> legalMoves;
    private PieceColor actualPlayer;
    private ChessRules chessRules = new ChessRules();
    private final ChessContext chessContext = new ChessContext(moveHistory);

    @Override
    public void makeMove(Move move) {
        if (move instanceof ChessMove) {
            validateMove((ChessMove) move);
            executeMove((ChessMove) move);
            postMoveUpdates((ChessMove) move);
        }
    }

    private void executeMove(ChessMove move) {
        move.apply(board);
        addToHistory(move);
    }

    private void validateMove(ChessMove move) {
        if (!legalMoves.contains(move)) {
            throw new MoveNotAvailableException(move);
        }
    }

    private void postMoveUpdates(ChessMove move) {
        actualPlayer = actualPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        legalMoves = chessRules.legalMoves(board, actualPlayer, chessContext);
    }

    public void addToHistory(ChessMove move) {
        moveHistory.add(move);
    }

    public void removeLastMoveFromHistory() {
        if(!moveHistory.isEmpty()){
            moveHistory.remove(moveHistory.size() - 1);
        }
    }
}
