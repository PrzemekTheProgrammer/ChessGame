package pszerszenowicz.infrastructure.web.history;

import org.springframework.stereotype.Component;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.games.chess.game.ChessGame;
import pszerszenowicz.games.chess.game.ChessMoveResolver;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveSnapshot;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;
import pszerszenowicz.infrastructure.web.game.ChessBoardMapper;
import pszerszenowicz.infrastructure.web.game.MoveResponse;
import pszerszenowicz.infrastructure.web.game.PlayerNameMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameHistoryMapper {

    private final ChessBoardMapper chessBoardMapper;
    private final PlayerNameMapper playerNameMapper;

    public GameHistoryMapper(
            ChessBoardMapper chessBoardMapper,
            PlayerNameMapper playerNameMapper
    ) {
        this.chessBoardMapper = chessBoardMapper;
        this.playerNameMapper = playerNameMapper;
    }

    public GameHistoryResponse toResponse(ChessGame game) {
        List<HistoryPositionResponse> positions =
                mapPositions(game.getMoveHistory());

        String white = playerNameMapper.map(
                game.playerOf(PieceColor.WHITE)
        );

        String black = playerNameMapper.map(
                game.playerOf(PieceColor.BLACK)
        );

        return new GameHistoryResponse(
                white,
                black,
                positions
        );
    }

    public List<HistoryPositionResponse> mapPositions(
            List<ChessMoveSnapshot> history
    ) {
        ChessBoard board = new ChessBoard();
        board.setBoard();

        ChessPosition position = new ChessPosition(board);

        List<HistoryPositionResponse> positions = new ArrayList<>();

        positions.add(new HistoryPositionResponse(
                0,
                chessBoardMapper.mapPieces(board),
                null
        ));

        int ply = 1;

        for (ChessMoveSnapshot snapshot : history) {
            ChessMove move = ChessMoveResolver.resolve(
                    position.legalMoves(),
                    snapshot
            );

            move.apply(position);

            positions.add(new HistoryPositionResponse(
                    ply,
                    chessBoardMapper.mapPieces(
                            position.getChessBoard()
                    ),
                    mapMove(snapshot)
            ));

            ply++;
        }

        return positions;
    }

    private MoveResponse mapMove(ChessMoveSnapshot move) {
        return new MoveResponse(
                ChessBoard.getCoordinateAsString(
                        move.from().getColumn(),
                        move.from().getRow()
                ),
                ChessBoard.getCoordinateAsString(
                        move.to().getColumn(),
                        move.to().getRow()
                )
        );
    }
}