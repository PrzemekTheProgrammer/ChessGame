package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.exception.MoveNotAvailableException;
import pszerszenowicz.domain.exception.MoveNotAvailableForPlayerException;
import pszerszenowicz.domain.exception.PlayerNotInGameException;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChessGame implements Game {

    private final ChessBoard board = new ChessBoard();
    private final List<ChessMove> moveHistory = new ArrayList<>();
    private Set<ChessMove> legalMoves;
    private PieceColor actualPlayer;
    private ChessPosition position;
//    private final ChessRules chessRules = new ChessRules();
    private GameStatus gameStatus;
    private final Player white;
    private final Player black;
    private final GameId gameId;

    public ChessGame(Player white, Player black) {
        this.white = Objects.requireNonNull(white);
        this.black = Objects.requireNonNull(black);
        this.gameId = GameId.random();
        initGame();
    }

    @Override
    public void makeMove(Move move, Player player) {
        validateTurn(player);
        if (gameStatus == GameStatus.ONGOING) {
            if (move instanceof ChessMove) {
                validateMove((ChessMove) move);
                executeMove((ChessMove) move);
                postMoveUpdates();
            }
        }
    }

    @Override
    public void initGame() {
        board.setBoard();
        gameStatus = GameStatus.ONGOING;
        position = new ChessPosition(board);
        legalMoves = position.legalMoves();
    }

    @Override
    public GameId getGameId() {
        return gameId;
    }

    @Override
    public boolean hasPlayer(Player p) {
        return p.equals(white) || p.equals(black);
    }

    private void executeMove(ChessMove move) {
        move.apply(position);
        addToHistory(move);
    }

    private void validateMove(ChessMove move) {
        if (!legalMoves.contains(move)) {
            throw new MoveNotAvailableException(move);
        }
    }

    private void postMoveUpdates() {
        actualPlayer = actualPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        legalMoves = position.legalMoves();
        gameStatus = position.evaluateGameState();
    }

    public void addToHistory(ChessMove move) {
        moveHistory.add(move);
    }

    private void validateTurn(Player player) {
        if (actualPlayer != colorOf(player)) {
            throw new MoveNotAvailableForPlayerException();
        }
    }

    public PieceColor colorOf(Player player) {
        if (player.equals(white)) return PieceColor.WHITE;
        if (player.equals(black)) return PieceColor.BLACK;
        throw new PlayerNotInGameException();
    }

}
