package pszerszenowicz.chess.board;

import org.junit.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.piece.*;

import java.util.List;

import static org.junit.Assert.*;
import static pszerszenowicz.games.chess.board.ChessBoard.*;


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

    @Test
    public void moveTest_Castle1() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,C1,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        board.applyMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(C1,D1)));
        assertSame(testedPiece.getPieceCoordinate(), C1);
        assertSame(tmpPiece.getPieceCoordinate(),D1);
    }

    @Test
    public void moveTest_Castle2() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E8,board.black());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.black());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,G8,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        board.applyMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(G8,F8)));
        assertSame(testedPiece.getPieceCoordinate(), G8);
        assertSame(tmpPiece.getPieceCoordinate(),F8);
    }

    @Test
    public void moveTest_Capture1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(E8,board.black());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,H8,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        board.applyMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(H8));
        assertSame(testedPiece.getPieceCoordinate(), H8);
        assertTrue(pieceCoordinates.size() == 1);
    }

    @Test
    public void moveTest_Capture2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(E1,board.black());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,A1,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        board.applyMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(A1,H8)));
        assertSame(testedPiece.getPieceCoordinate(), A1);
        assertTrue(pieceCoordinates.size() == 2);
    }

    @Test
    public void moveTest_EnPassant1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(E4,board.black());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F4,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,F3,tmpPiece);
        move.addTag(ChessMoveTags.EnPassant);
        move.addTag(ChessMoveTags.Capture);
        board.applyMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(F3));
        assertSame(testedPiece.getPieceCoordinate(), F3);
        assertTrue(pieceCoordinates.size() == 1);
    }

    @Test
    public void undoMoveTest1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,C1,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        board.applyMove(move);
        board.undoMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,A1)));
        assertSame(testedPiece.getPieceCoordinate(), E1);
        assertSame(tmpPiece.getPieceCoordinate(),A1);
        assertTrue(pieceCoordinates.size() == 2);
    }

    @Test
    public void undoMoveTest2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(E1,board.black());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,board.white());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,A1,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        board.applyMove(move);
        board.undoMove(move);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,H8,A1)));
        assertSame(testedPiece.getPieceCoordinate(), E1);
        assertTrue(pieceCoordinates.size() == 3);
    }

}
