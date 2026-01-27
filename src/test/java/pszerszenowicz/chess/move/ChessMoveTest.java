package pszerszenowicz.chess.move;

import org.junit.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.piece.King;
import pszerszenowicz.games.chess.piece.Pawn;
import pszerszenowicz.games.chess.piece.Queen;
import pszerszenowicz.games.chess.piece.Rook;

import java.util.List;

import static org.junit.Assert.*;
import static pszerszenowicz.games.chess.board.ChessBoard.*;

public class ChessMoveTest {
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
        move.apply(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(C1,D1)));
        assertSame(C1, testedPiece.getPieceCoordinate());
        assertSame(D1, tmpPiece.getPieceCoordinate());
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
        move.apply(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(G8,F8)));
        assertSame(G8, testedPiece.getPieceCoordinate());
        assertSame(F8, tmpPiece.getPieceCoordinate());
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
        move.apply(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(H8));
        assertSame(H8, testedPiece.getPieceCoordinate());
        assertEquals(1, pieceCoordinates.size());
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
        move.apply(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(A1,H8)));
        assertSame(A1, testedPiece.getPieceCoordinate());
        assertEquals(2, pieceCoordinates.size());
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
        move.apply(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(F3));
        assertSame(F3, testedPiece.getPieceCoordinate());
        assertEquals(1, pieceCoordinates.size());
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
        move.apply(board);
        move.undo(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,A1)));
        assertSame(E1, testedPiece.getPieceCoordinate());
        assertSame(A1, tmpPiece.getPieceCoordinate());
        assertEquals(2, pieceCoordinates.size());
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
        move.apply(board);
        move.undo(board);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,H8,A1)));
        assertSame(E1, testedPiece.getPieceCoordinate());
        assertEquals(3, pieceCoordinates.size());
    }

}
