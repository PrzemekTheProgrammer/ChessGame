package pszerszenowicz.domain.ai;

import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;

import java.util.Comparator;
import java.util.List;

public class MoveOrdering {

    public List<ChessMove> order(List<ChessMove> moves) {
        return moves.stream()
                .sorted(Comparator.comparingInt(this::score).reversed())
                .toList();
    }

    private int score(ChessMove move) {
        int score = 0;

        if (move.hasTag(ChessMoveTags.PROMOTE_QUEEN)) {
            score += 20_000;
        }

        if (move.hasTag(ChessMoveTags.PROMOTE_BISHOP)) {
            score += 15000;
        }

        if (move.hasTag(ChessMoveTags.PROMOTE_KNIGHT)) {
            score += 15000;
        }

        if (move.hasTag(ChessMoveTags.PROMOTE_ROOK)) {
            score += 15000;
        }

        if (move.hasTag(ChessMoveTags.CAPTURE)) {
            int attacker = move.piece().getValue();
            int victim = move.getAuxillaryPiece().getValue();
            score += victim * 10;
            score -= attacker * 10;
        }

        if (move.hasTag(ChessMoveTags.ATTACKS_KING)) {
            score += 1_000;
        }

        if(move.hasTag(ChessMoveTags.CASTLE)){
            score += 500;
        }

        return score;
    }
}