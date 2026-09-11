package pszerszenowicz.domain.ai;

import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.game.GameStatus;

public class AiTeacher {

    private static final int INFINITY = Integer.MAX_VALUE - 1000;
    private final BoardEvaluator evaluator;

    public AiTeacher(BoardEvaluator evaluator) {
        this.evaluator = new HeuristicsEvaluator();
    }

    public int evaluateGameWithDeph(ChessGame game, int depth) {
        int maxScore = -INFINITY;
        int alpha = -INFINITY;
        int beta = INFINITY;

        for (Move move : game.getPosition().legalMoves()) {
            ChessGame gameCopy = new ChessGame(game);
            Player player = game.playerOf(game.getPosition().getSideToMove());
            gameCopy.makeMove(move, player);
            int score = -negamaxWithDeph(gameCopy, depth - 1, -beta, -alpha);

            if (score > maxScore) {
                maxScore = score;
            }

            alpha = Math.max(alpha, score);
        }
        return maxScore;
    }

    private int negamaxWithDeph(ChessGame game, int depth, int alpha, int beta) {
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
            int score = -negamaxWithDeph(gameCopy, depth - 1, -beta, -alpha);

            maxScore = Math.max(maxScore, score);
            alpha = Math.max(alpha, score);

            if (alpha >= beta) {
                break;
            }
        }
        return maxScore;
    }
}
