package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Pawn;
import pszerszenowicz.chess.adapters.piece.Rook;
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

public class RookTest {

    @Test
    public void getRookMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Rook(D4,board.getWhite());
        board.addPiece(testedPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(14,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(A4,B4,C4,E4,F4,G4,H4,D1,D2,D3,D5,D6,D7,D8)));
    }
    @Test
    public void getRookMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Rook(A1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A4,board.getWhite());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(9,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(B1,C1,D1,E1,F1,G1,H1,A2,A3)));
    }
    @Test
    public void getRookMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Rook(D4,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(D3,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D5,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C4,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E4,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(C4));
        assertTrue(rookMoveCoordinate.contains(E4));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getRookMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Rook(A3,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A2,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(A4,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(A4,B3)));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getRookMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new Rook(H5,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H1,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(H7,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new King(D5,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> rookMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(9,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(H4,H3,H2,H6,H7,G5,F5,E5,D5)));
        assertTrue(movesTags.contains(MoveTags.Capture));
        assertTrue(movesTags.contains(MoveTags.AttacksKing));
    }
}
