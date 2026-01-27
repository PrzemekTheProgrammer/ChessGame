package pszerszenowicz.chess.board;

import org.junit.Test;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.piece.*;


import static org.junit.Assert.*;


public class ChessBoardTests {

    @Test
    public void boardIsSet() {
        //given
        Board board = new ChessBoard();
        //when
        board.setBoard();

        long piecesQuantity = board.pieces().size();

        PieceColor white = board.white();
        PieceColor black = board.black();
        long whitePiecesQuantity = board.pieces()
                .stream().filter((piece) -> piece.getColor() == white)
                .count();
        long blackPiecesQuantity = board.pieces()
                .stream().filter((piece) -> piece.getColor() == black)
                .count();

        long pawnsQuantity = board.pieces()
                .stream().filter((piece) -> piece instanceof Pawn)
                .count();
        long rooksQuantity = board.pieces()
                .stream().filter((piece) -> piece instanceof Rook)
                .count();
        long knightsQuantity = board.pieces()
                .stream().filter((piece) -> piece instanceof Knight)
                .count();
        long bishopsQuantity = board.pieces()
                .stream().filter((piece) -> piece instanceof Bishop)
                .count();
        long queensQuantity = board.pieces()
                .stream().filter((piece) -> piece instanceof Queen)
                .count();
        long kingsQuantity = board.pieces()
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
