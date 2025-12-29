package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
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

public class PawnTest {

    @Test
    public void getPawnMoves_MoveForwardOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(B3,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        //then
        assertEquals(1,pawnMoves.size());
        assertEquals(B4, pawnMoves.iterator().next().getTo());
    }

    @Test
    public void getPawnMoves_MoveForwardAndCharge() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(B2,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(B3));
        assertTrue(pawnMoveCoordinate.contains(B4));
        assertTrue(movesTags.contains(MoveTags.Charge));
    }

    @Test
    public void getPawnMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(B2,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        List<MoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,pawnMoves.size());
        assertEquals(C3,pawnMoves.iterator().next().getTo());
        assertTrue(movesTags.contains(MoveTags.Capture));
    }

    @Test
    public void getPawnMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(B2,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(A3,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(C3));
        assertTrue(pawnMoveCoordinate.contains(A3));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }

    @Test
    public void getPawnMoves_EnPassantOnly() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(B5,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B6,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(C6));
        assertTrue(movesTags.contains(MoveTags.Capture));
        assertTrue(movesTags.contains(MoveTags.EnPassant));
    }

    @Test
    public void getPawnMoves_AttacksKing() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Pawn(A7,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new King(B8,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(A8));
        assertTrue(pawnMoveCoordinate.contains(B8));
        assertTrue(movesTags.contains(MoveTags.AttacksKing));
    }

}
