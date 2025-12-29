package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Knight;
import pszerszenowicz.chess.adapters.piece.Pawn;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pszerszenowicz.chess.adapters.board.ChessBoard.*;

public class KnightTest {
    @Test
    public void getKnightMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Knight(D4,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(8,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C2,B3,B5,C6,E6,F5,F3,E2)));
    }
    @Test
    public void getKnightMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Knight(H7,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(F8,F6,G5)));
    }
    @Test
    public void getKnightMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Knight(A8,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C7,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B6,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,knightMoves.size());
        assertTrue(knightMoveCoordinate.contains(B6));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getKnightMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Knight(G7,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E8,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E6,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(H5,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(E6,F5,H5)));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getKnightMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Knight(B2,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A4,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new King(C4,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D3,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C4,D3,D1)));
        assertTrue(movesTags.contains(MoveTags.Capture));
        assertTrue(movesTags.contains(MoveTags.AttacksKing));
    }
}
