package pszerszenowicz.chess;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.*;
import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.board.Board;

import static org.junit.Assert.*;

public class ChessBoardTests {

    @Test
    public void boardIsSet() {
        //given
        Board board = new ChessBoard();
        //when
        board.setBoard();

        long piecesQuantity = board.getPieceCoordinate().size();

        Player white = board.getWhite();
        Player black = board.getBlack();
        long whitePiecesQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece.getPlayer() == white)
                .count();
        long blackPiecesQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece.getPlayer() == black)
                .count();

        long pawnsQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof Pawn)
                .count();
        long rooksQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof Rook)
                .count();
        long knightsQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof Knight)
                .count();
        long bishopsQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof Bishop)
                .count();
        long queensQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof Queen)
                .count();
        long kingsQuantity = board.getPieceCoordinate().values()
                .stream().filter((piece) -> piece instanceof King)
                .count();
        //Then
        assertEquals(32, piecesQuantity);
        assertEquals(16, blackPiecesQuantity);
        assertEquals(16, whitePiecesQuantity);
        assertEquals(16, pawnsQuantity);
        assertEquals(4, rooksQuantity);
        assertEquals(4, knightsQuantity);
        assertEquals(4, bishopsQuantity);
        assertEquals(2, queensQuantity);
        assertEquals(2, kingsQuantity);
    }


}
