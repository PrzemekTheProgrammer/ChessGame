package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.game.GameStatus;

public class AiEngine {
    private static final int INFINITY = Integer.MAX_VALUE - 1000;
    private final BoardEvaluator evaluator;

    public AiEngine(BoardEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public Move findBestMoveWithTimeLimit(ChessGame game, long timeLimitMs) {
        long deadline = System.currentTimeMillis() + timeLimitMs;
        Move bestMoveOverall = null;
        int currentDepth = 1;

        try {
            while (System.currentTimeMillis() < deadline) {
                Move bestMoveForDepth = searchAtDepthWithDeadline(game, currentDepth, deadline);
                if (bestMoveForDepth != null) {
                    bestMoveOverall = bestMoveForDepth; // Przypisujemy wynik tylko po ukończeniu całej głębokości
                }
                currentDepth++;
            }
        } catch (TimeOutException e) {
            // Przerwanie wykonywania z powodu upływu czasu
        }

        return bestMoveOverall != null ? bestMoveOverall : game.getPosition().legalMoves().stream().toList().getFirst();
    }

    private Move searchAtDepthWithDeadline(ChessGame game, int depth, long deadline) throws TimeOutException {
        Move bestMove = null;
        int maxScore = -INFINITY;
        int alpha = -INFINITY;
        int beta = INFINITY;

        for (Move move : game.getPosition().legalMoves()) {
            checkTimeout(deadline);
            ChessGame gameCopy = new ChessGame(game);
            Player player = game.playerOf(game.getPosition().getSideToMove());
            gameCopy.makeMove(move, player);
            int score = -negamaxWithDeadline(gameCopy, depth - 1, -beta, -alpha, deadline);

            if (score > maxScore) {
                maxScore = score;
                bestMove = move;
            }
            alpha = Math.max(alpha, score);
        }
        return bestMove;
    }

    private int negamaxWithDeadline(ChessGame game, int depth, int alpha, int beta, long deadline) throws TimeOutException {
        checkTimeout(deadline);
        GameStatus gameStatus = game.getStatus();
        if (gameStatus != GameStatus.ONGOING) {
            if (gameStatus.equals(GameStatus.BLACK_WIN) || gameStatus.equals(GameStatus.WHITE_WIN)) return -INFINITY - depth;
            if (gameStatus.equals(GameStatus.STALEMATE)) return 0;
        }
        if (depth == 0) {
            return evaluator.evaluate(game.getPosition());
        }

        int maxScore = -INFINITY;
        for (Move move : game.getPosition().legalMoves()) {
            ChessGame gameCopy = new ChessGame(game);
            Player player = game.playerOf(game.getPosition().getSideToMove());
            gameCopy.makeMove(move, player);
            int score = -negamaxWithDeadline(gameCopy, depth - 1, -beta, -alpha, deadline);

            maxScore = Math.max(maxScore, score);
            alpha = Math.max(alpha, score);

            if (alpha >= beta) {
                break;
            }
        }
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
}
