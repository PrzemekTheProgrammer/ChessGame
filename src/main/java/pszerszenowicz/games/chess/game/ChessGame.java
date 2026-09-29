package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.exception.MoveNotAvailableException;
import pszerszenowicz.domain.exception.MoveNotAvailableForPlayerException;
import pszerszenowicz.domain.exception.PlayerNotInGameException;
import pszerszenowicz.domain.ports.game.Game;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveSnapshot;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;
import pszerszenowicz.games.chess.position.ZobristHasher;

import java.util.*;

public class ChessGame implements Game {

    private final ChessBoard board;
    private final List<ChessMoveSnapshot> moveHistory = new ArrayList<>();
    private Set<ChessMove> legalMoves;
    private ChessPosition position;
    private GameStatus gameStatus;
    private final Player white;
    private final Player black;
    private final GameId gameId;
    private final Map<Long, Integer> positionOccurrences = new HashMap<>();

    public ChessGame(Player white, Player black) {
        this(GameId.random(), white, black);
    }

    public ChessGame(
            GameId gameId,
            Player white,
            Player black
    ) {
        this.board = new ChessBoard();
        this.white = Objects.requireNonNull(white);
        this.black = Objects.requireNonNull(black);
        this.gameId = Objects.requireNonNull(gameId);
        initGame();
    }

    public ChessGame(ChessGame chessGame) {
        this.board = new ChessBoard(chessGame.board);
        this.white = chessGame.white;
        this.black = chessGame.black;
        this.gameId = GameId.of(UUID.randomUUID());
        this.position = new ChessPosition(chessGame.position);
        this.gameStatus = chessGame.gameStatus;
        this.legalMoves = chessGame.legalMoves;
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
        moveHistory.clear();
        positionOccurrences.clear();
        long hash = ZobristHasher.repetitionHash(position);
        positionOccurrences.put(hash, 1);
        legalMoves = position.legalMoves();
    }

    @Override
    public GameStatus getStatus() {
        return gameStatus;
    }

    @Override
    public ChessPosition getPosition() {
        return position;
    }

    @Override
    public GameId getGameId() {
        return gameId;
    }

    @Override
    public boolean hasPlayer(Player p) {
        return p.equals(white) || p.equals(black);
    }

    public ChessMove findLegalMove(
            PieceCoordinate from,
            PieceCoordinate to,
            ChessMoveTags promotion
    ) {
        return ChessMoveResolver.resolve(
                legalMoves,
                from,
                to,
                promotion);
    }

    public ChessMove findLegalMove(
            ChessMoveSnapshot chessMoveSnapshot
    ) {
        return ChessMoveResolver.resolve(
                legalMoves,
                chessMoveSnapshot
        );
    }

    public ChessMove findLegalMove(ChessMove searchedMove) {
        return legalMoves.stream()
                .filter(move ->
                        move.from().equals(searchedMove.from())
                                && move.to().equals(searchedMove.to())
                                && move.getTags().equals(searchedMove.getTags())
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Legal move not found: "
                                        + searchedMove.from()
                                        + " -> "
                                        + searchedMove.to()
                        )
                );
    }

    public List<ChessMoveSnapshot> getMoveHistory() {
        return moveHistory;
    }

    public PieceColor colorOf(Player player) {
        if (player.equals(white)) return PieceColor.WHITE;
        if (player.equals(black)) return PieceColor.BLACK;
        throw new PlayerNotInGameException();
    }

    public Player playerOf(PieceColor pieceColor) {
        if (pieceColor.equals(PieceColor.WHITE)) return white;
        if (pieceColor.equals(PieceColor.BLACK)) return black;
        throw new NullPointerException();
    }

    private void executeMove(ChessMove move) {
        move.apply(position);
        addToHistory(move);
    }

    public void addToHistory(ChessMove move) {
        moveHistory.add(ChessMoveSnapshot.from(move));
    }

    public void replay(List<ChessMoveSnapshot> history) {
        for (ChessMoveSnapshot snapshot : history) {
            Player player = playerOf(position.getSideToMove());
            ChessMove move = findLegalMove(snapshot);

            makeMove(move, player);
        }
    }

    private void validateMove(ChessMove move) {
        boolean available = legalMoves.stream()
                .anyMatch(legalMove ->
                        legalMove.from().equals(move.from())
                                && legalMove.to().equals(move.to())
                                && legalMove.getTags().equals(move.getTags())
                );

        if (!available) {
            throw new MoveNotAvailableException(move);
        }
    }

    private void postMoveUpdates() {
        legalMoves = position.legalMoves();
        registerPosition();
        gameStatus = isThreefoldRepetition() ? GameStatus.STALEMATE : position.evaluateGameState();
    }


    private void validateTurn(Player player) {
        if (position.getSideToMove() != colorOf(player)) {
            throw new MoveNotAvailableForPlayerException();
        }
    }


    private void registerPosition() {

        long hash = ZobristHasher.repetitionHash(position);

        int count = positionOccurrences.merge(
                hash,
                1,
                Integer::sum
        );
        System.out.println(
                "REGISTER POSITION: hash=" + hash
                        + ", count=" + count
                        + ", side=" + position.getSideToMove()
        );
    }

    private boolean isThreefoldRepetition() {

        long hash = ZobristHasher.repetitionHash(position);
        int count = positionOccurrences.getOrDefault(hash, 0);
        System.out.println(
                "THREEFOLD CHECK: hash=" + hash
                        + ", count=" + count
                        + ", side=" + position.getSideToMove()
        );
        return positionOccurrences.getOrDefault(hash, 0) >= 3;
    }

    private boolean isPromotionTag(ChessMoveTags tag) {
        return switch (tag) {
            case PROMOTE_QUEEN,
                 PROMOTE_ROOK,
                 PROMOTE_BISHOP,
                 PROMOTE_KNIGHT -> true;
            default -> false;
        };
    }


    private boolean promotionMatches(
            ChessMove move,
            ChessMoveTags promotion
    ) {
        if (promotion != null) {
            return move.hasTag(promotion);
        }

        return move.getTags()
                .stream()
                .noneMatch(this::isPromotionTag);
    }

}
