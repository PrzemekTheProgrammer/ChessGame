package pszerszenowicz.infrastructure.web.history;

import pszerszenowicz.infrastructure.web.game.MoveResponse;
import pszerszenowicz.infrastructure.web.game.PieceResponse;

import java.util.List;

public record HistoryPositionResponse(
        int ply,
        List<PieceResponse> pieces,
        MoveResponse lastMove
) {
}

