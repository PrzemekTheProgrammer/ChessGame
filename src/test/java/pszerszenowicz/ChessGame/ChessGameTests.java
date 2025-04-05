package pszerszenowicz.ChessGame;

import org.junit.Test;
import pszerszenowicz.ChessGame.adapters.board.ChessBoardImpl;
import pszerszenowicz.ChessGame.adapters.pieces.*;
import pszerszenowicz.adapters.Player;
import pszerszenowicz.domain.ports.board.Board;

import static org.junit.Assert.*;

public class ChessGameTests {

    @Test
    public void boardIsSet() {
        //given
        Board board = new ChessBoardImpl();
        //when
        board.setBoard();

        long piecesQuantity = board.getPieces().size();

        Player white = board.getPlayerOne();
        Player black = board.getPlayerTwo();
        long whitePiecesQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece.getPlayer() == white)
                .count();
        long blackPiecesQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece.getPlayer() == black)
                .count();

        long pawnsQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece instanceof Pawn)
                .count();
        long rooksQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece instanceof Rook)
                .count();
        long knightsQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece instanceof Knight)
                .count();
        long bishopsQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece instanceof Bishop)
                .count();
        long queensQuantity = board.getPieces().values()
                .stream().filter((piece) -> piece instanceof Queen)
                .count();
        long kingsQuantity = board.getPieces().values()
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
