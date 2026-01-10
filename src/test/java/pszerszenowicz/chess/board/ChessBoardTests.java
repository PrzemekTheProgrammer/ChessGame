package pszerszenowicz.chess.board;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.*;
import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.List;

import static org.junit.Assert.*;
import static pszerszenowicz.chess.adapters.board.ChessBoard.*;


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

    @Test
    public void moveTest_Castle1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,C1,tmpPiece);
        move.addTag(MoveTags.Castle);
        board.move(move);
        List<Piece> pieces = board.getPieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(C1,D1)));
        assertSame(testedPiece.getPieceCoordinate(), C1);
        assertSame(tmpPiece.getPieceCoordinate(),D1);
        assertEquals(board.getPieceCoordinate().values().stream().toList(),pieces);
    }

    @Test
    public void moveTest_Castle2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E8,board.getBlack());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.getBlack());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,G8,tmpPiece);
        move.addTag(MoveTags.Castle);
        board.move(move);
        List<Piece> pieces = board.getPieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(G8,F8)));
        assertSame(testedPiece.getPieceCoordinate(), G8);
        assertSame(tmpPiece.getPieceCoordinate(),F8);
        assertEquals(board.getPieceCoordinate().values().stream().toList(),pieces);
    }

    @Test
    public void moveTest_Capture1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(E8,board.getBlack());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,H8,tmpPiece);
        move.addTag(MoveTags.Capture);
        board.move(move);
        List<Piece> pieces = board.getPieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(H8));
        assertSame(testedPiece.getPieceCoordinate(), H8);
        assertTrue(pieceCoordinates.size() == 1);
        assertEquals(board.getPieceCoordinate().values().stream().toList(),pieces);
    }

    @Test
    public void moveTest_Capture2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(E1,board.getBlack());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,A1,tmpPiece);
        move.addTag(MoveTags.Capture);
        board.move(move);
        List<Piece> pieces = board.getPieces();
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
        Piece testedPiece = new Pawn(E4,board.getBlack());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F4,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,F3,tmpPiece);
        move.addTag(MoveTags.EnPassant);
        move.addTag(MoveTags.Capture);
        board.move(move);
        List<Piece> pieces = board.getPieces();
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
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,C1,tmpPiece);
        move.addTag(MoveTags.Castle);
        board.move(move);
        board.undoMove(move);
        List<Piece> pieces = board.getPieces();
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
        Piece testedPiece = new Queen(E1,board.getBlack());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,board.getWhite());
        board.addPiece(tmpPiece);
        Move move = new Move(testedPiece,A1,tmpPiece);
        move.addTag(MoveTags.Capture);
        board.move(move);
        board.undoMove(move);
        List<Piece> pieces = board.getPieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,H8,A1)));
        assertSame(testedPiece.getPieceCoordinate(), E1);
        assertTrue(pieceCoordinates.size() == 3);
    }

}
