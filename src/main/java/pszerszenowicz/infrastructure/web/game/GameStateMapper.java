package pszerszenowicz.infrastructure.web.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.piece.*;
import pszerszenowicz.games.chess.position.ChessBoard;

import java.util.List;

@Component
public class GameStateMapper {

    public GameStateResponse toResponse(
            ChessGame game,
            UserId currentUser
    ) {
        return new GameStateResponse(
                game.getGameId().id(),
                game.getStatus(),
                getPlayerColor(game, currentUser),
                game.getPosition().getSideToMove(),
                mapPieces(game),
                mapLegalMoves(game)
        );
    }

    private List<PieceResponse> mapPieces(ChessGame game) {
        return game.getPosition()
                .getChessBoard()
                .pieces()
                .stream()
                .map(piece -> new PieceResponse(
                        ChessBoard.getCoordinateAsString(
                                piece.getPieceCoordinate().getColumn(),
                                piece.getPieceCoordinate().getRow()
                        ),
                        getPieceType(piece),
                        piece.getColor()
                ))
                .toList();
    }

    private List<MoveResponse> mapLegalMoves(ChessGame game) {
        return game.getPosition()
                .legalMoves()
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

    private String getPieceType(Piece piece) {
        return switch (piece) {
            case Pawn ignored -> "PAWN";
            case Knight ignored -> "KNIGHT";
            case Bishop ignored -> "BISHOP";
            case Rook ignored -> "ROOK";
            case Queen ignored -> "QUEEN";
            case King ignored -> "KING";
            default -> throw new IllegalArgumentException(
                    "Unknown piece type: " + piece.getClass()
            );
        };
    }
}