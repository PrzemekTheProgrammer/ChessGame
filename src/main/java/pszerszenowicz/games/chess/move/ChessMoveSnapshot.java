package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.core.piece.PieceCoordinate;

public record ChessMoveSnapshot(
        PieceCoordinate from,
        PieceCoordinate to,
        ChessMoveTags promotion
) {

    public static ChessMoveSnapshot from(ChessMove move) {
        ChessMoveTags promotion = move.getTags()
                .stream()
                .filter(ChessMoveSnapshot::isPromotion)
                .findFirst()
                .orElse(null);

        return new ChessMoveSnapshot(
                move.from(),
                move.to(),
                promotion
        );
    }

    private static boolean isPromotion(ChessMoveTags tag) {
        return switch (tag) {
            case PROMOTE_QUEEN,
                 PROMOTE_ROOK,
                 PROMOTE_BISHOP,
                 PROMOTE_KNIGHT -> true;
            default -> false;
        };
    }
}