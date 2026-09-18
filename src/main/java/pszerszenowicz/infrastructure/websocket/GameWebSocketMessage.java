package pszerszenowicz.infrastructure.websocket;

public record GameWebSocketMessage(
        String type,
        String from,
        String to,
        String promotion
) {
}