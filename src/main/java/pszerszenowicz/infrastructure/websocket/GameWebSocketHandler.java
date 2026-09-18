package pszerszenowicz.infrastructure.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.player.BotPlayer;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.exception.PlayerNotInGameException;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.infrastructure.web.game.GameStateMapper;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameService gameService;
    private final GameStateMapper gameStateMapper;
    private final ObjectMapper objectMapper;
    private final Executor aiExecutor;
    private final Set<GameId> gamesWithRunningAi = ConcurrentHashMap.newKeySet();
    private final Map<GameId, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public GameWebSocketHandler(
            GameService gameService,
            GameStateMapper gameStateMapper,
            ObjectMapper objectMapper,
            Executor aiExecutor
    ) {
        this.gameService = gameService;
        this.gameStateMapper = gameStateMapper;
        this.objectMapper = objectMapper;
        this.aiExecutor = aiExecutor;
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status
    ) {
        GameId gameId = extractGameId(session);

        Set<WebSocketSession> gameSessions =
                sessions.get(gameId);

        if (gameSessions == null) {
            return;
        }

        gameSessions.remove(session);

        if (gameSessions.isEmpty()) {
            sessions.remove(gameId, gameSessions);
        }
        System.out.println(
                "WS CONNECTION CLOSED: "
                        + session.getId()
                        + ", status=" + status
        );
    }

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session
    ) throws Exception {

        UUID rawUserId = (UUID) session
                .getAttributes()
                .get(JwtHandshakeInterceptor.USER_ID);

        UserId userId = UserId.of(rawUserId);
        GameId gameId = extractGameId(session);

        runBotIfNeeded(gameId);
        System.out.println(
                "WS CONNECTION ESTABLISHED: "
                        + session.getId()
        );
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session,
            TextMessage message
    ) throws Exception {

        GameWebSocketMessage request =
                objectMapper.readValue(
                        message.getPayload(),
                        GameWebSocketMessage.class
                );

        if (!"MOVE".equals(request.type())) {
            return;
        }

        UUID rawUserId = (UUID) session
                .getAttributes()
                .get(JwtHandshakeInterceptor.USER_ID);

        UserId userId = UserId.of(rawUserId);

        GameId gameId = extractGameId(session);

        ChessGame game = gameService.findGame(gameId);

        Player player = getPlayer(game, userId);

        PieceCoordinate from =
                ChessBoard.getCoordinate(request.from());

        PieceCoordinate to =
                ChessBoard.getCoordinate(request.to());

        ChessMove move = game.findLegalMove(from, to);

        gameService.makeMove(gameId, move, player);

        // człowiek natychmiast widzi swój ruch
        broadcastState(gameId);

        // AI liczy już poza wątkiem WebSocket
        runBotIfNeeded(gameId);

    }

    private void broadcastState(GameId gameId) {
        Set<WebSocketSession> gameSessions =
                sessions.get(gameId);

        if (gameSessions == null) {
            return;
        }

        ChessGame game = gameService.findGame(gameId);

        for (WebSocketSession session : gameSessions) {
            if (!session.isOpen()) {
                continue;
            }

            try {
                UUID rawUserId = (UUID) session
                        .getAttributes()
                        .get(JwtHandshakeInterceptor.USER_ID);

                UserId userId = UserId.of(rawUserId);

                var response =
                        gameStateMapper.toResponse(
                                game,
                                userId
                        );

                String json =
                        objectMapper.writeValueAsString(response);

                session.sendMessage(
                        new TextMessage(json)
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private GameId extractGameId(WebSocketSession session) {
        String path = session.getUri().getPath();

        String id = path.substring(
                path.lastIndexOf('/') + 1
        );

        return GameId.of(UUID.fromString(id));
    }

    private Player getPlayer(
            ChessGame game,
            UserId userId
    ) {
        Player white = game.playerOf(PieceColor.WHITE);
        Player black = game.playerOf(PieceColor.BLACK);

        if (white instanceof HumanPlayer human
                && human.getUserId().equals(userId)) {
            return white;
        }

        if (black instanceof HumanPlayer human
                && human.getUserId().equals(userId)) {
            return black;
        }

        throw new PlayerNotInGameException();
    }

    private void runBotIfNeeded(
            GameId gameId
    ) {
        System.out.println(
                "RUN BOT CHECK: game=" + gameId
        );
        if (!gamesWithRunningAi.add(gameId)) {
            return;
        }

        try {
            ChessGame game = gameService.findGame(gameId);

            Player currentPlayer =
                    game.playerOf(game.getPosition().getSideToMove());

            System.out.println(
                    "RUN BOT CHECK: side="
                            + game.getPosition().getSideToMove()
                            + ", player="
                            + currentPlayer.getClass().getSimpleName()
            );

            if (!(currentPlayer instanceof BotPlayer)) {
                gamesWithRunningAi.remove(gameId);
                return;
            }

            aiExecutor.execute(() -> {
                try {
                    gameService.makeBotMove(gameId, 3000);

                    System.out.println("BOT MOVE: broadcasting");

                    broadcastState(gameId);

                    System.out.println("BOT MOVE: broadcast finished");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    gamesWithRunningAi.remove(gameId);
                }
            });

        } catch (Exception e) {
            gamesWithRunningAi.remove(gameId);
            throw e;
        }
    }

}