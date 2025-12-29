package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.Bishop;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Pawn;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pszerszenowicz.chess.adapters.board.ChessBoard.*;

public class BishopTest {

    @Test
    public void getBishopMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Bishop(A1,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(7,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(B2,C3,D4,E5,F6,G7,H8)));
    }

    @Test
    public void getBishopMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Bishop(D4,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(13,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(A7,B6,C5,E3,F2,G1,A1,B2,C3,E5,F6,G7,H8)));
    }
    @Test
    public void getBishopMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Bishop(D4,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.contains(E3));
        assertTrue(bishopMoveCoordinate.contains(E5));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getBishopMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Bishop(D4,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.contains(E3));
        assertTrue(bishopMoveCoordinate.contains(E5));
        assertTrue(bishopMoveCoordinate.contains(C3));
        assertTrue(bishopMoveCoordinate.contains(C5));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getBishopMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Bishop(D4,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(B2,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new King(F6,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(C3,C5,E3,E5,F6)));
        assertTrue(movesTags.containsAll(Set.of(MoveTags.Capture, MoveTags.AttacksKing)));
    }

}
