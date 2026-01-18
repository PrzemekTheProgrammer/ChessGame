package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.domain.ports.GameContext;
import pszerszenowicz.domain.ports.Rules;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;

import java.util.Set;
import java.util.stream.Collectors;

public class ChessRules implements Rules {
    @Override
    public Set<ChessMove> legalMoves(Board board, PieceColor color, GameContext context) {
        if (!(board instanceof ChessBoard chessBoard) ||
                !(context instanceof ChessContext chessContext)) {
            return Set.of();
        }

        PieceColor opponentColor = (color == PieceColor.WHITE)
                ? PieceColor.BLACK
                : PieceColor.WHITE;

        return chessBoard.availableMoves(color).stream()
                .filter(ChessMove.class::isInstance)
                .map(ChessMove.class::cast)
                .filter(move -> isMoveLegal(move, opponentColor, chessBoard, chessContext))
                .collect(Collectors.toSet());
    }

    private boolean isMoveLegal(ChessMove move, PieceColor oponnentColor, ChessBoard board, ChessContext context) {
        board.applyMove(move);
        Set<ChessMove> opponentMoves = board.availableMoves(oponnentColor);

        boolean kingAttacked = opponentMoves.stream()
                .anyMatch(m -> m.getTags().contains(ChessMoveTags.AttacksKing));
        board.undoMove(move);
        if (kingAttacked) {
            board.undoMove(move);
            return false;
        }

        opponentMoves = board.availableMoves(oponnentColor);

        if (move.hasTag(ChessMoveTags.Castle) && !isCastleLegal(move, opponentMoves)) {
            return false;
        }

        if (move.hasTag(ChessMoveTags.EnPassant) && !isEnPassantLegal(move, context)) {
            return false;
        }

        board.undoMove(move);
        return true;
    }

    private boolean isCastleLegal(ChessMove move, Set<ChessMove> opponentMoves) {
        int dir = move.to().getColumn() > 5 ? -1 : 1;
        int passingColumn = move.to().getColumn() + dir;

        return opponentMoves.stream().noneMatch(m ->
                m.to().getColumn() == passingColumn ||
                        m.getTags().contains(ChessMoveTags.AttacksKing)
        );
    }

    private boolean isEnPassantLegal(ChessMove move, ChessContext context) {
        return context.lastMove()
                .filter(lastMove ->
                        lastMove.hasTag(ChessMoveTags.Charge)
                                && lastMove.to().getColumn() == move.to().getColumn()
                ).isPresent();
    }
}
