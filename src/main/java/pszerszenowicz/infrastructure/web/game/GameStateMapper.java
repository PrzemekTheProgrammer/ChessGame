package pszerszenowicz.infrastructure.web.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.player.BotPlayer;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveSnapshot;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.List;

@Component
public class GameStateMapper {

    private final ChessBoardMapper chessBoardMapper;
    private final PlayerNameMapper playerNameMapper;

    public GameStateMapper(
            ChessBoardMapper chessBoardMapper,
            PlayerNameMapper playerNameMapper
    ) {
        this.chessBoardMapper = chessBoardMapper;
        this.playerNameMapper = playerNameMapper;
    }

    public GameStateResponse toResponse(
            ChessGame game,
            UserId currentUser
    ) {
        return new GameStateResponse(
                game.getGameId().id(),
                game.getStatus(),
                getPlayerColor(game, currentUser),
                game.getPosition().getSideToMove(),
                chessBoardMapper.mapPieces(
                        game.getPosition().getChessBoard()
                ),
                mapLegalMoves(game),
                getOpponent(game, currentUser),
                mapLastMove(game)
        );
    }

    private String getOpponent(
            ChessGame game,
            UserId currentUser
    ) {
        Player white = game.playerOf(PieceColor.WHITE);
        Player black = game.playerOf(PieceColor.BLACK);

        Player opponent;

        if (isUser(white, currentUser)) {
            opponent = black;
        } else if (isUser(black, currentUser)) {
            opponent = white;
        } else {
            throw new IllegalArgumentException(
                    "User is not a player in this game"
            );
        }
        return playerNameMapper.map(opponent);
    }

    private MoveResponse mapLastMove(ChessGame game) {
        List<ChessMoveSnapshot> history = game.getMoveHistory();

        if (history.isEmpty()) {
            return null;
        }

        return mapMove(history.getLast());
    }

    private List<MoveResponse> mapLegalMoves(ChessGame game) {
        ChessPosition position = new ChessPosition(game.getPosition());

        return position.legalMoves()
                .stream()
                .map(this::mapMove)
                .toList();
    }

    private MoveResponse mapMove(ChessMove move) {
        return new MoveResponse(
                ChessBoard.getCoordinateAsString(
                        move.from().getColumn(),
                        move.from().getRow()
                ),
                ChessBoard.getCoordinateAsString(
                        move.to().getColumn(),
                        move.to().getRow()
                )
        );
    }

    private MoveResponse mapMove(ChessMoveSnapshot move) {
        return new MoveResponse(
                ChessBoard.getCoordinateAsString(
                        move.from().getColumn(),
                        move.from().getRow()
                ),
                ChessBoard.getCoordinateAsString(
                        move.to().getColumn(),
                        move.to().getRow()
                )
        );
    }

    private PieceColor getPlayerColor(
            ChessGame game,
            UserId currentUser
    ) {
        Player white = game.playerOf(PieceColor.WHITE);
        Player black = game.playerOf(PieceColor.BLACK);

        if (isUser(white, currentUser)) {
            return PieceColor.WHITE;
        }

        if (isUser(black, currentUser)) {
            return PieceColor.BLACK;
        }

        throw new IllegalArgumentException(
                "User is not a player in this game"
        );
    }

    private boolean isUser(
            Player player,
            UserId userId
    ) {
        return player instanceof HumanPlayer human
                && human.getUserId().equals(userId);
    }

}