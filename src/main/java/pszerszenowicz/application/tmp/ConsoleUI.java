package pszerszenowicz.application.tmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pszerszenowicz.application.game.GameService;
import pszerszenowicz.application.player.BotPlayer;
import pszerszenowicz.application.player.DefaultPlayerFactory;
import pszerszenowicz.domain.ai.AiType;
import pszerszenowicz.domain.core.game.GameId;
import pszerszenowicz.domain.core.user.UserId;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Player;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.game.GameStatus;

import java.util.Scanner;

@Component
public class ConsoleUI implements CommandLineRunner {

    private final MoveMapper moveMapper = new MoveMapper();

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private GameService gameService;

    @Autowired
    private DefaultPlayerFactory defaultPlayerFactory;


    @Override
    public void run(String... args){
        Player bot = defaultPlayerFactory.createBot(AiType.HEURISTIC);
        Player player = defaultPlayerFactory.createHuman(UserId.random());
        GameId gameId = gameService.createGame(bot, player);
        ChessGame game = (ChessGame) gameService.findGame(gameId);
        Move aiMove;
        while (game.getStatus() == GameStatus.ONGOING) {
            aiMove = ((BotPlayer)bot).findBestMove(game.getPosition(),10000);
            gameService.makeMove(gameId, aiMove, bot);
            System.out.println("Ruch komputera: ");
            System.out.println(moveMapper.MoveToString(aiMove));
            if(game.getStatus() != GameStatus.ONGOING) {
                break;
            }
            System.out.println("Twój ruch: ");
            Move m = null;
            do {
                String choice = scanner.nextLine();
                try {
                    m = moveMapper.StringToMove(choice, game.getPosition().legalMoves());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } while (m == null);
            gameService.makeMove(gameId,m, player);
        }
        System.exit(0);
    }
}
