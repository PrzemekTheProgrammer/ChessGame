package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ai.transposition.TranspositionEntry;
import pszerszenowicz.domain.ai.transposition.TranspositionTable;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.ChessPosition;
import pszerszenowicz.games.chess.position.RepetitionTracker;
import pszerszenowicz.games.chess.position.ZobristHasher;

import java.util.List;

public class AiEngine {
    private static final int INFINITY = Integer.MAX_VALUE - 1000;
    private final BoardEvaluator evaluator;
    private final TranspositionTable transpositionTable;
    private final RepetitionTracker repetitionTracker = new RepetitionTracker();
    private final MoveOrdering moveOrdering = new MoveOrdering();
    private long totalNodes;
    private long nodes;
    private long ttHits;
    private long betaCutoffs;
    long legalMoveGenerationTime;

    public AiEngine(BoardEvaluator evaluator, TranspositionTable transpositionTable) {
        this.evaluator = evaluator;
        this.transpositionTable = transpositionTable;
    }

    public Move findBestMoveWithTimeLimit(ChessPosition position, long timeLimitMs) {
        long deadline = System.currentTimeMillis() + timeLimitMs;
        Move bestMoveOverall = null;
        int currentDepth = 1;
        totalNodes = 0;
        legalMoveGenerationTime = 0;

        try {
            while (System.currentTimeMillis() < deadline) {
                Move bestMoveForDepth = searchAtDepthWithDeadline(position, currentDepth, deadline);
                if (bestMoveForDepth != null) {
                    bestMoveOverall = bestMoveForDepth; // Przypisujemy wynik tylko po ukończeniu całej głębokości
                }
                System.out.println("Deph = " + currentDepth);
                System.out.println("nodes = " + nodes + ", ttHits = " + ttHits + ", betaCutoffs = " + betaCutoffs);
                currentDepth++;
            }
        } catch (TimeOutException e) {
            // Przerwanie wykonywania z powodu upływu czasu
            System.out.println("Zakończono myślenie z deph=" + (currentDepth - 1));
            System.out.println("TotalNodes = " + totalNodes);
            System.out.println("legalMove total generation time = " + legalMoveGenerationTime / 1_000_000_000.0);
        }

        return bestMoveOverall != null ? bestMoveOverall : position.legalMoves().stream().toList().getFirst();
    }

    private ChessMove searchAtDepthWithDeadline(
            ChessPosition position,
            int depth,
            long deadline
    ) throws TimeOutException {

        ChessMove bestMove = null;
        int maxScore = -INFINITY;
        int alpha = -INFINITY;
        int beta = INFINITY;
        nodes = 0;
        ttHits = 0;
        betaCutoffs = 0;
        long start = System.nanoTime();
        List<ChessMove> m = moveOrdering.order(position.legalMoves().stream().toList());
        legalMoveGenerationTime = legalMoveGenerationTime + System.nanoTime() - start;
        for (ChessMove move : m) {
            checkTimeout(deadline);

            move.apply(position);
            repetitionTracker.add(position);

            int score;
            try {
                score = -negamaxWithDeadline(
                        position,
                        depth - 1,
                        -beta,
                        -alpha,
                        deadline
                );
            } finally {
                repetitionTracker.remove(position);
                move.undo(position);
            }

            if (score > maxScore) {
                maxScore = score;
                bestMove = move;
            }

            alpha = Math.max(alpha, score);
        }

        return bestMove;
    }

    private int negamaxWithDeadline(
            ChessPosition position,
            int depth,
            int alpha,
            int beta,
            long deadline
    ) throws TimeOutException {

        nodes++;
        totalNodes++;
        checkTimeout(deadline);
        int originalAlpha = alpha;
        int originalBeta = beta;
        long hash = ZobristHasher.hash(position);
        TranspositionEntry entry = transpositionTable.get(hash);
        if (repetitionTracker.isThreefoldRepetition(position)) {
            return 0;
        }

        if (position.getHalfMoveClock() >= 100) {
            return 0;
        }

        if (entry != null && entry.depth() >= depth) {
            ttHits++;
            switch (entry.bound()) {
                case EXACT -> {
                    return entry.score();
                }
                case LOWER -> {
                    alpha = Math.max(alpha, entry.score());
                }
                case UPPER -> {
                    beta = Math.min(beta, entry.score());
                }
            }
            if (alpha >= beta) {
                return entry.score();
            }
        }

        long start = System.nanoTime();
        List<ChessMove> m = position.legalMoves().stream().toList();
        legalMoveGenerationTime = legalMoveGenerationTime + System.nanoTime() - start;
        List<ChessMove> legalMoves = moveOrdering.order(m);
        if (legalMoves.isEmpty()) {
            return isKingAttacked(position)
                    ? -INFINITY - depth
                    : 0;
        }


        if (depth == 0) {
            int score = evaluator.evaluate(position);

            transpositionTable.put(
                    hash,
                    new TranspositionEntry(
                            0,
                            score,
                            TranspositionEntry.Bound.EXACT,
                            hash
                    )
            );

            return score;
        }

        int maxScore = -INFINITY;

        for (ChessMove move : legalMoves) {

            if ((totalNodes & 2047) == 0) {
                checkTimeout(deadline);
            }

            move.apply(position);

            move.apply(position);
            repetitionTracker.add(position);

            int score;
            try {
                score = -negamaxWithDeadline(
                        position,
                        depth - 1,
                        -beta,
                        -alpha,
                        deadline
                );
            } finally {
                repetitionTracker.remove(position);
                move.undo(position);
            }

            maxScore = Math.max(maxScore, score);
            alpha = Math.max(alpha, score);

            if (alpha >= beta) {
                betaCutoffs++;
                break;
            }
        }

        TranspositionEntry.Bound bound;

        if (maxScore <= originalAlpha) {
            bound = TranspositionEntry.Bound.UPPER;
        } else if (maxScore >= originalBeta) {
            bound = TranspositionEntry.Bound.LOWER;
        } else {
            bound = TranspositionEntry.Bound.EXACT;
        }

        transpositionTable.put(
                hash,
                new TranspositionEntry(
                        depth,
                        maxScore,
                        bound,
                        hash
                )
        );

        return maxScore;
    }

    private void checkTimeout(long deadline) throws TimeOutException {
        if (System.currentTimeMillis() >= deadline) {
            throw new TimeOutException();
        }
    }

    private static class TimeOutException extends Exception {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this; // Wyłącza zbieranie stack trace'a – natychmiastowy "skok" z rekurencji
        }
    }

    private boolean isKingAttacked(ChessPosition position) {
        position.oppositeSideToMove();
        try {
            return position.availableMoves()
                    .stream()
                    .anyMatch(move -> move.hasTag(ChessMoveTags.ATTACKS_KING));
        } finally {
            position.oppositeSideToMove();
        }
    }
}
