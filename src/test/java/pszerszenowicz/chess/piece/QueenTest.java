package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Pawn;
import pszerszenowicz.chess.adapters.piece.Queen;
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

public class QueenTest {

    @Test
    public void getQueenMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(A2,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> queenMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> queenMoveCoordinate = queenMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(21,queenMoves.size());
        assertTrue(queenMoveCoordinate.containsAll(Set.of(
                A1,A3,A4,A5,A6,A7,A8,
                B1,
                B2,C2,D2,E2,F2,G2,H2,
                B3,C4,D5,E6,F7,G8)));
    }

    @Test
    public void getQueenMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(F5,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F7,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.getWhite());
        board.addPiece(tmpPiece);
        Set<Move> queenMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> queenMoveCoordinate = queenMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(18,queenMoves.size());
        assertTrue(queenMoveCoordinate.containsAll(Set.of(
                F4,F3,F2,F1,
                F6,
                G5,H5,
                E6,D7,C8,
                G6,H7,
                E4,D3,C2,B1,
                G4,H3)));
    }
    @Test
    public void getQueenMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(D4,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D3,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C4,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E4,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D5,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(C4));
        assertTrue(rookMoveCoordinate.contains(E4));
        assertTrue(rookMoveCoordinate.contains(C5));
        assertTrue(rookMoveCoordinate.contains(D5));
        assertTrue(rookMoveCoordinate.contains(E5));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getQueenMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(A1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A2,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B1,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B2,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(A2));
        assertTrue(rookMoveCoordinate.contains(B1));
        assertTrue(rookMoveCoordinate.contains(B2));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getQueenMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Queen(H8,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H7,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new King(G8,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G7,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(G7));
        assertTrue(rookMoveCoordinate.contains(G8));
        assertTrue(movesTags.contains(MoveTags.Capture));
        assertTrue(movesTags.contains(MoveTags.AttacksKing));
    }
}
