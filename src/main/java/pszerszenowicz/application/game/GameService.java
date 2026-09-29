package pszerszenowicz.application.game;

import org.springframework.stereotype.Service;
import pszerszenowicz.application.exception.GameNotFoundException;
import pszerszenowicz.application.player.BotPlayer;
import pszerszenowicz.application.player.HumanPlayer;
import pszerszenowicz.application.ports.PlayerFactory;
import pszerszenowicz.application.ports.game.GameRepository;
import pszerszenowicz.application.ports.user.UserRepository;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.game.GameStatus;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;
import pszerszenowicz.domain.core.user.User;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

@Service
public class GameService {
    private final GameRepository repo;
    private final UserRepository userRepository;
    private final PlayerFactory playerFactory;
    private final Random random = new Random();
    private final ConcurrentHashMap<GameId, ReentrantLock> gameLocks = new ConcurrentHashMap<>();

    public GameService(
            GameRepository repo,
            PlayerFactory playerFactory,
            UserRepository userRepository
    ) {
        this.repo = repo;
        this.playerFactory = playerFactory;
        this.userRepository = userRepository;
    }

    public GameId createGame(Player p1, Player p2) {
        ChessGame game = new ChessGame(p1, p2);
        repo.save(game);
        return game.getGameId();
    }

    public void makeMove(GameId id, Move move, Player player) {
        ChessGame game = repo.find(id)
                .orElseThrow(GameNotFoundException::new);
        game.makeMove(move, player);
        repo.save(game);
    }

    public GameId createBotGame(
            UserId userId,
            AiType aiType,
            PlayerColorChoice colorChoice
    ) {
        Player human = playerFactory.createHuman(userId);
        Player bot = playerFactory.createBot(aiType);

        boolean humanWhite = switch (colorChoice) {
            case WHITE -> true;
            case BLACK -> false;
            case RANDOM -> random.nextBoolean();
        };

        if (humanWhite) {
            return createGame(human, bot);
        }

        return createGame(bot, human);
    }

    public ChessGame findGame(GameId id) {
        return repo.find(id)
                .orElseThrow(GameNotFoundException::new);
    }

    public List<GameSummaryResult> findGamesByUser(UserId userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(game -> toSummary(game, userId))
                .toList();
    }

    public List<GameSummaryResult> findGamesByUser(
            UserId userId,
            GameListFilter filter
    ) {
        return repo.findByUserId(userId)
                .stream()
                .filter(game -> matchesFilter(game, filter))
                .map(game -> toSummary(game, userId))
                .toList();
    }

    public void makeBotMove(GameId id, long timeLimitMs) {
        ReentrantLock lock = lockFor(id);

        ChessPosition searchPosition;
        BotPlayer bot;

        lock.lock();
        try {
            ChessGame game = repo.find(id)
                    .orElseThrow(GameNotFoundException::new);

            Player currentPlayer =
                    game.playerOf(game.getPosition().getSideToMove());

            if (!(currentPlayer instanceof BotPlayer currentBot)) {
                return;
            }

            bot = currentBot;
            searchPosition = new ChessPosition(game.getPosition());

        } finally {
            lock.unlock();
        }

        ChessMove searchedMove = (ChessMove) bot.findBestMove(
                searchPosition,
                timeLimitMs
        );

        ChessGame game = repo.find(id)
                .orElseThrow(GameNotFoundException::new);

        ChessMove realMove = game.findLegalMove(searchedMove);

        game.makeMove(realMove, bot);
        repo.save(game);
    }

    public <T> T withGame(
            GameId id,
            Function<ChessGame, T> operation
    ) {
        ReentrantLock lock = lockFor(id);
        lock.lock();

        try {
            ChessGame game = repo.find(id)
                    .orElseThrow(GameNotFoundException::new);
            return operation.apply(game);
        } finally {
            lock.unlock();
        }
    }

    public <T> T withGameForUser(
            GameId id,
            UserId userId,
            Function<ChessGame, T> operation
    ) {
        return withGame(id, game -> {
            if (!hasUser(game, userId)) {
                throw new GameNotFoundException();
            }

            return operation.apply(game);
        });
    }

    private boolean matchesFilter(
            ChessGame game,
            GameListFilter filter
    ) {
        return switch (filter) {
            case ONGOING ->
                    game.getStatus() == GameStatus.ONGOING;

            case FINISHED ->
                    game.getStatus() != GameStatus.ONGOING;
        };
    }

    private boolean hasUser(
            ChessGame game,
            UserId userId
    ) {
        return isUser(game.playerOf(PieceColor.WHITE), userId)
                || isUser(game.playerOf(PieceColor.BLACK), userId);
    }

    private boolean isUser(
            Player player,
            UserId userId
    ) {
        return player instanceof HumanPlayer human
                && human.getUserId().equals(userId);
    }

    private GameSummaryResult toSummary(
            ChessGame game,
            UserId userId
    ) {
        Player white = game.playerOf(PieceColor.WHITE);
        Player black = game.playerOf(PieceColor.BLACK);

        boolean userIsWhite =
                white instanceof HumanPlayer human
                        && human.getUserId().equals(userId);

        Player opponent = userIsWhite ? black : white;

        return new GameSummaryResult(
                game.getGameId().id(),
                opponentName(opponent),
                userIsWhite ? "WHITE" : "BLACK",
                game.getStatus()
        );
    }

    private String opponentName(Player opponent) {
        if (opponent instanceof HumanPlayer human) {
            return userRepository
                    .findById(human.getUserId().uuid())
                    .map(User::getUserName)
                    .orElse("Nieznany gracz");
        }

        if (opponent instanceof BotPlayer) {
            return "Bot";
        }

        return "Nieznany gracz";
    }

    private ReentrantLock lockFor(GameId gameId) {
        return gameLocks.computeIfAbsent(
                gameId,
                id -> new ReentrantLock()
        );
    }

}
