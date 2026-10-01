package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveSnapshot;
import pszerszenowicz.games.chess.move.ChessMoveTags;

import java.util.Collection;
import java.util.List;

public final class ChessMoveResolver {

    private ChessMoveResolver() {
    }

    public static ChessMove resolve(
            Collection<ChessMove> legalMoves,
            PieceCoordinate from,
            PieceCoordinate to,
            ChessMoveTags promotion
    ) {
        if (promotion != null && !isPromotionTag(promotion)) {
            throw new IllegalArgumentException(
                    "Invalid promotion tag: " + promotion
            );
        }

        return legalMoves.stream()
                .filter(move ->
                        move.from().equals(from)
                                && move.to().equals(to)
                                && promotionMatches(move, promotion)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Legal move not found: "
                                        + from + " -> " + to
                                        + ", promotion=" + promotion
                        )
                );
    }

    public static ChessMove resolve(
            Collection<ChessMove> legalMoves,
            ChessMoveSnapshot snapshot
    ) {
        return resolve(
                legalMoves,
                snapshot.from(),
                snapshot.to(),
                snapshot.promotion()
        );
    }

    private static boolean promotionMatches(
            ChessMove move,
            ChessMoveTags promotion
    ) {
        if (promotion != null) {
            return move.hasTag(promotion);
        }

        return move.getTags()
                .stream()
                .noneMatch(ChessMoveResolver::isPromotionTag);
    }

    private static boolean isPromotionTag(ChessMoveTags tag) {
        return switch (tag) {
            case PROMOTE_QUEEN,
                 PROMOTE_ROOK,
                 PROMOTE_BISHOP,
                 PROMOTE_KNIGHT -> true;
            default -> false;
        };
    }
}