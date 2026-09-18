package pszerszenowicz.infrastructure.web.game;

import pszerszenowicz.games.chess.game.GameStatus;
import pszerszenowicz.domain.core.piece.PieceColor;

import java.util.List;
import java.util.UUID;

public record GameStateResponse(
        UUID gameId,
        GameStatus status,
        PieceColor myColor,
        PieceColor sideToMove,
        List<PieceResponse> pieces,
        List<MoveResponse> legalMoves
) {
}