package pszerszenowicz.chess.domain;

import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Rook;
import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.exception.MoveNotAvailableException;
import pszerszenowicz.ports.game.Game;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.move.MoveTags;

import java.util.Set;
import java.util.stream.Collectors;

public class ChessGame extends Game {
    public ChessGame() {
        super(new ChessBoard());
    }

    @Override
    public Set<Move> getLegalMoves(Player player) {
        Player actual = getActualPlayer();
        Player opponent = actual == getBoard().getWhite()
                ? getBoard().getBlack()
                : getBoard().getWhite();

        return getBoard().avaibleMoves(actual).stream()
                .filter(move -> isMoveLegal(move, opponent))
                .collect(Collectors.toSet());
    }

    @Override
    public void makeMove(Move move) {
        validateMove(move);
        executeMove(move);
        postMoveUpdates(move);
    }

    private boolean isMoveLegal(Move move, Player opponent) {
        Board board = getBoard();
        board.move(move);

        Set<Move> opponentMoves = board.avaibleMoves(opponent);

        boolean kingAttacked = opponentMoves.stream()
                .anyMatch(m -> m.getTags().contains(MoveTags.AttacksKing));

        if (kingAttacked) {
            board.undoMove(move);
            return false;
        }

        if (move.hasTag(MoveTags.Castle) && !isCastleLegal(move, opponentMoves)) {
            board.undoMove(move);
            return false;
        }

        if (move.hasTag(MoveTags.EnPassant) && !isEnPassantLegal(move)) {
            board.undoMove(move);
            return false;
        }

        board.undoMove(move);
        return true;
    }

    private boolean isCastleLegal(Move move, Set<Move> opponentMoves) {
        int dir = move.getTo().getColumn() > 5 ? -1 : 1;
        int passingColumn = move.getTo().getColumn() + dir;

        return opponentMoves.stream().noneMatch(m ->
                m.getTo().getColumn() == passingColumn ||
                        m.getTags().contains(MoveTags.AttacksKing)
        );
    }

    private boolean isEnPassantLegal(Move move) {
        Move lastMove = getLastMove();
        return lastMove != null
                && lastMove.hasTag(MoveTags.Charge)
                && lastMove.getTo().getColumn() == move.getTo().getColumn();
    }

    private void executeMove(Move move) {
        getBoard().move(move);
        addToHistory(move);
    }

    private void validateMove(Move move) {
        if (!legalMoves.contains(move)) {
            throw new MoveNotAvailableException(move);
        }
    }

    private void postMoveUpdates(Move move) {
        if (move.getPiece() instanceof King king) {
            king.loseCastleRight();
        }
        if (move.getPiece() instanceof Rook rook) {
            rook.loseCastleRight();
        }
        setActualPlayer(getActualPlayer() == getBoard().getWhite() ? getBoard().getBlack() : getBoard().getWhite());
        legalMoves = getLegalMoves(getActualPlayer());
    }
}
