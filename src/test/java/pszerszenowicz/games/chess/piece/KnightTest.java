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

public class KnightTest {
    @Test
    public void getKnightMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Knight testedPiece = new Knight(D4,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(8,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C2,B3,B5,C6,E6,F5,F3,E2)));
    }
    @Test
    public void getKnightMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Knight testedPiece = new Knight(H7,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(F8,F6,G5)));
    }
    @Test
    public void getKnightMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Knight testedPiece = new Knight(A8,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C7,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B6,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,knightMoves.size());
        assertTrue(knightMoveCoordinate.contains(B6));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }
    @Test
    public void getKnightMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Knight testedPiece = new Knight(G7,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E8,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E6,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F5,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(H5,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(E6,F5,H5)));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }
    @Test
    public void getKnightMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Knight testedPiece = new Knight(B2,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A4,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new King(C4,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D3,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C4,D3,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
        assertTrue(movesTags.contains(ChessMoveTags.AttacksKing));
    }
}
