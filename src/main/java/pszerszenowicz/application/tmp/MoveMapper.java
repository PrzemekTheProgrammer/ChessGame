package pszerszenowicz.application.tmp;

import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.position.ChessBoard;

import java.util.Set;

public class MoveMapper {

    public String MoveToString(Move move) {
        return ChessBoard.getCoordinateAsString(move.from().getColumn(), move.from().getRow())
                + "->" + ChessBoard.getCoordinateAsString(move.to().getColumn(), move.to().getRow());
    }

    public ChessMove StringToMove(String s, Set<ChessMove> legalMoves) {
        String s1 = s.substring(0, 2);
        String s2 = s.substring(2, 4);
        return legalMoves.stream().filter(m ->
                        ChessBoard.getCoordinateAsString(m.from().getColumn(), m.from().getRow()).equals(s1)
                                && ChessBoard.getCoordinateAsString(m.to().getColumn(), m.to().getRow()).equals(s2)
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nie znaleziono ruchu: " + s1 + "->" + s2
                ));
    }

}
