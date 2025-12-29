package pszerszenowicz.chess.piece;

import org.junit.Test;
import pszerszenowicz.chess.adapters.board.ChessBoard;
import pszerszenowicz.chess.adapters.piece.King;
import pszerszenowicz.chess.adapters.piece.Pawn;
import pszerszenowicz.chess.adapters.piece.Rook;
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

public class KingTest {

    @Test
    public void getKingMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(D6,board.getWhite());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(8,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(C7,D7,E7,C6,E6,C5,D5,E5)));
    }
    @Test
    public void getKingMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(A8,board.getWhite());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A7,board.getWhite());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(B8,B7)));
    }
    @Test
    public void getKingMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F1,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F2,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D1,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F2,E2,D2,D1)));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }

    @Test
    public void getKingMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(H1,board.getWhite());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H2,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G1,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G2,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(G1,G2)));
        assertTrue(movesTags.contains(MoveTags.Capture));
    }
    @Test
    public void getKingMoves_Castle1() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.getWhite());
        ((Rook) tmpPiece).loseCastleRight();
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,board.getWhite());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(6,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1)));
        assertTrue(movesTags.contains(MoveTags.Castle));
    }
    @Test
    public void getKingMoves_Castle2() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,board.getWhite());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(7,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1,C1)));
        assertTrue(movesTags.contains(MoveTags.Castle));
    }
    @Test
    public void getKingMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Piece testedPiece = new King(E1,board.getWhite());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H1,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,board.getWhite());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,board.getBlack());
        board.addPiece(tmpPiece);
        tmpPiece = new King(F2,board.getBlack());
        board.addPiece(tmpPiece);
        Set<Move> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(Move::getTo).toList();
        List<MoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,F2,D1)));
        assertTrue(movesTags.contains(MoveTags.Castle));
        assertTrue(movesTags.contains(MoveTags.Capture));
        assertTrue(movesTags.contains(MoveTags.AttacksKing));
    }


}
