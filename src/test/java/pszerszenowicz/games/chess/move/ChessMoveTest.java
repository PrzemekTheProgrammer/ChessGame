package pszerszenowicz.games.chess.move;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.piece.King;
import pszerszenowicz.games.chess.piece.Pawn;
import pszerszenowicz.games.chess.piece.Queen;
import pszerszenowicz.games.chess.piece.Rook;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class ChessMoveTest {
    @Test
    public void moveTest_Castle1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E1, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,C1,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        move.apply(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E8,PieceColor.BLACK);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,G8,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        move.apply(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Queen(E8,PieceColor.BLACK);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,H8,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        move.apply(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Queen(E1,PieceColor.BLACK);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,A1,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        move.apply(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Pawn(E4,PieceColor.BLACK);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F4,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,F3,tmpPiece);
        move.addTag(ChessMoveTags.EnPassant);
        move.addTag(ChessMoveTags.Capture);
        move.apply(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new King(E1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,C1,tmpPiece);
        move.addTag(ChessMoveTags.Castle);
        move.apply(position);
        move.undo(position);
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Queen(E1,PieceColor.BLACK);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H8,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(testedPiece,A1,tmpPiece);
        move.addTag(ChessMoveTags.Capture);
        move.apply(position);
        move.undo(position);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.containsAll(List.of(E1,H8,A1)));
        assertSame(E1, testedPiece.getPieceCoordinate());
        assertEquals(3, pieceCoordinates.size());
    }

    @Test
    public void promoteTest01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Pawn(B7,PieceColor.BLACK);
        board.addPiece(testedPiece);
        ChessMove move = new ChessMove(testedPiece,B8,testedPiece);
        move.addTag(ChessMoveTags.PROMOTE_QUEEN);
        move.apply(position);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(B8));
        assertEquals(1, pieces.size());
        assertInstanceOf(Queen.class, board.getPiece(B8));
    }

    @Test
    public void undoPromoteTest01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Piece testedPiece = new Pawn(B7,PieceColor.BLACK);
        board.addPiece(testedPiece);
        ChessMove move = new ChessMove(testedPiece,B8,testedPiece);
        move.addTag(ChessMoveTags.PROMOTE_QUEEN);
        move.apply(position);
        move.undo(position);
        List<Piece> pieces = board.pieces();
        List<PieceCoordinate> pieceCoordinates = pieces.stream().map(Piece::getPieceCoordinate).toList();
        //then
        assertTrue(pieceCoordinates.contains(B7));
        assertEquals(1, pieces.size());
        assertInstanceOf(Pawn.class, board.getPiece(B7));
    }

}
