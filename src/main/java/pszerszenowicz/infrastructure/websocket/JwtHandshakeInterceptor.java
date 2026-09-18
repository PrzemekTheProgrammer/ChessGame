package pszerszenowicz.infrastructure.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import pszerszenowicz.infrastructure.security.jwt.JwtService;

import java.util.Map;
import java.util.UUID;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    public static final String USER_ID = "userId";

    private final JwtService jwtService;

    public JwtHandshakeInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        System.out.println("WS HANDSHAKE: " + request.getURI());

        String token = UriComponentsBuilder
                .fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token");

        if (token == null || token.isBlank()) {
            System.out.println("WS HANDSHAKE: NO TOKEN");
            return false;
        }

        try {
            UUID userId = jwtService.parse(token);

            System.out.println(
                    "WS HANDSHAKE OK: userId=" + userId
            );

            attributes.put(USER_ID, userId);
            return true;

        } catch (Exception e) {
            System.out.println(
                    "WS HANDSHAKE JWT ERROR: "
                            + e.getClass().getSimpleName()
                            + ": "
                            + e.getMessage()
            );
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
    }
}