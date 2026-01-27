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
    public Set<ChessMove> legalMoves(Board board, PieceColor actualPlayer, GameContext context) {
        if (!(board instanceof ChessBoard chessBoard) ||
                !(context instanceof ChessContext chessContext)) {
            return Set.of();
        }

        PieceColor opponentColor = (actualPlayer == PieceColor.WHITE)
                ? PieceColor.BLACK
                : PieceColor.WHITE;

        return chessBoard.availableMoves(actualPlayer).stream()
                .filter(ChessMove.class::isInstance)
                .map(ChessMove.class::cast)
                .filter(move -> isMoveLegal(move, opponentColor, chessBoard, chessContext))
                .collect(Collectors.toSet());
    }

    private boolean isMoveLegal(ChessMove move, PieceColor oponnentColor, ChessBoard board, ChessContext context) {
        move.apply(board);
        Set<ChessMove> opponentMoves = board.availableMoves(oponnentColor);

        boolean kingAttacked = opponentMoves.stream()
                .anyMatch(m -> m.getTags().contains(ChessMoveTags.AttacksKing));
        move.undo(board);
        if (kingAttacked) {
            return false;
        }

        opponentMoves = board.availableMoves(oponnentColor);

        if (move.hasTag(ChessMoveTags.Castle) && !isCastleLegal(move, opponentMoves)) {
            return false;
        }

        if (move.hasTag(ChessMoveTags.EnPassant) && !isEnPassantLegal(move, context)) {
            return false;
        }
        return true;
    }

    private boolean isCastleLegal(ChessMove move, Set<ChessMove> opponentMoves) {
        int dir = move.to().getColumn() > move.from().getColumn() ? 1 : -1;
        int row = move.from().getRow();
        for(int passingColumn = move.from().getColumn()+dir; passingColumn != move.to().getColumn() + dir; passingColumn+=dir) {
            final int col = passingColumn;
            if (opponentMoves.stream().anyMatch(m ->
                    (m.to().getColumn() == col && m.to().getRow()==row)
            || m.hasTag(ChessMoveTags.AttacksKing))){
                return false;
            }
        }
        return true;
    }

    private boolean isEnPassantLegal(ChessMove move, ChessContext context) {
        return context.lastMove()
                .filter(lastMove ->
                        lastMove.hasTag(ChessMoveTags.Charge)
                                && lastMove.to().getColumn() == move.to().getColumn()
                ).isPresent();
    }
}
