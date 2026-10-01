package pszerszenowicz.infrastructure.web.game;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.games.chess.piece.*;
import pszerszenowicz.games.chess.position.ChessBoard;

import java.util.List;

@Component
public class ChessBoardMapper {

    public List<PieceResponse> mapPieces(ChessBoard board) {
        return board.pieces()
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
