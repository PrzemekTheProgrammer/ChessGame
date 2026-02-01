package pszerszenowicz.games.chess.piece;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pszerszenowicz.games.chess.board.ChessBoard.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PawnTest {

    @Test
    public void getPawnMoves_MoveForwardOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B3,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        //then
        assertEquals(1,pawnMoves.size());
        assertEquals(B4, pawnMoves.iterator().next().to());
    }

    @Test
    public void getPawnMoves_MoveForwardAndCharge() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B2,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(B3));
        assertTrue(pawnMoveCoordinate.contains(B4));
        assertTrue(movesTags.contains(ChessMoveTags.Charge));
    }

    @Test
    public void getPawnMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B2,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,pawnMoves.size());
        assertEquals(C3,pawnMoves.iterator().next().to());
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }

    @Test
    public void getPawnMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B2,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(A3,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(C3));
        assertTrue(pawnMoveCoordinate.contains(A3));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }

    @Test
    public void getPawnMoves_EnPassantOnly() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B5,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C5,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B6,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(C6));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
        assertTrue(movesTags.contains(ChessMoveTags.EnPassant));
    }

    @Test
    public void getPawnMoves_AttacksKing() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(A7,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new King(B8,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new King(A8,board.white());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(B8));
        assertTrue(movesTags.contains(ChessMoveTags.AttacksKing));
    }
    @Test
    public void getPawnMoves_Promote01() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(A7,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).toList();
        Set<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).collect(Collectors.toSet());
        //then
        assertEquals(4,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.contains(A8));
        assertTrue(movesTags.containsAll(Set.of(
                ChessMoveTags.PROMOTE_BISHOP,
                ChessMoveTags.PROMOTE_KNIGHT,
                ChessMoveTags.PROMOTE_ROOK,
                ChessMoveTags.PROMOTE_QUEEN)));
    }

    @Test
    public void getPawnMoves_Promote02() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(A7,board.white());
        board.addPiece(testedPiece);
        Queen tmpPiece = new Queen(B8, board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        Set<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).collect(Collectors.toSet());
        Set<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).collect(Collectors.toSet());
        //then
        assertEquals(8,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.containsAll(Set.of(A8,B8)));
        assertTrue(movesTags.containsAll(Set.of(
                ChessMoveTags.PROMOTE_BISHOP,
                ChessMoveTags.PROMOTE_KNIGHT,
                ChessMoveTags.PROMOTE_ROOK,
                ChessMoveTags.PROMOTE_QUEEN)));
    }

    @Test
    public void getPawnMoves_Promote03() {
        //given
        Board board = new ChessBoard();
        //when
        Pawn testedPiece = new Pawn(B7,board.white());
        board.addPiece(testedPiece);
        Queen tmpPiece = new Queen(A8, board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Queen(C8, board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> pawnMoves = testedPiece.getMoves(board);
        Set<PieceCoordinate> pawnMoveCoordinate = pawnMoves.stream().map(ChessMove::to).collect(Collectors.toSet());
        Set<ChessMoveTags> movesTags = pawnMoves.stream().flatMap(move -> move.getTags().stream()).collect(Collectors.toSet());
        //then
        assertEquals(12,pawnMoves.size());
        assertTrue(pawnMoveCoordinate.containsAll(Set.of(A8,B8,C8)));
        assertTrue(movesTags.containsAll(Set.of(
                ChessMoveTags.PROMOTE_BISHOP,
                ChessMoveTags.PROMOTE_KNIGHT,
                ChessMoveTags.PROMOTE_ROOK,
                ChessMoveTags.PROMOTE_QUEEN)));
    }
}
