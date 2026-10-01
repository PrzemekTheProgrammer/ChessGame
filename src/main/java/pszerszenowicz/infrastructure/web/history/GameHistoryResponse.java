package pszerszenowicz.infrastructure.web.history;

import java.util.List;

public record GameHistoryResponse(
        String white,
        String black,
        List<HistoryPositionResponse> positions
) {
}
