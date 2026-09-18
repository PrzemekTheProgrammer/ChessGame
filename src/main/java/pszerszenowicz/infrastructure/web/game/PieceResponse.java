package pszerszenowicz.infrastructure.web.game;

import pszerszenowicz.domain.core.piece.PieceColor;

public record PieceResponse(
        String square,
        String type,
        PieceColor color
) {
}