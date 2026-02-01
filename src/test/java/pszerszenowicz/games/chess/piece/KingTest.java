package pszerszenowicz.games.chess.piece;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;


import java.util.List;
import java.util.Set;

import static pszerszenowicz.games.chess.board.ChessBoard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KingTest {

    @Test
    public void getKingMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(D6,board.white());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(8,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(C7,D7,E7,C6,E6,C5,D5,E5)));
    }
    @Test
    public void getKingMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(A8,board.white());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A7,board.white());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(B8,B7)));
    }
    @Test
    public void getKingMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F1,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F2,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D1,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F2,E2,D2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }

    @Test
    public void getKingMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(H1,board.white());
        ((King) testedPiece).loseCastleRight();
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H2,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G1,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G2,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(G1,G2)));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }
    @Test
    public void getKingMoves_Castle1() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.white());
        ((Rook) tmpPiece).loseCastleRight();
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,board.white());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(6,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.Castle));
    }
    @Test
    public void getKingMoves_Castle2() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,board.white());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(7,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1,C1)));
        assertTrue(movesTags.contains(ChessMoveTags.Castle));
    }
    @Test
    public void getKingMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        King testedPiece = new King(E1,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H1,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new King(F2,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,F2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.Castle));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
        assertTrue(movesTags.contains(ChessMoveTags.AttacksKing));
    }


}
