package pszerszenowicz.games.chess.piece;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class KnightTest {
    @Test
    public void getKnightMoves_MoveOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Knight testedPiece = new Knight(D4,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(8,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C2,B3,B5,C6,E6,F5,F3,E2)));
    }
    @Test
    public void getKnightMoves_MoveOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Knight testedPiece = new Knight(H7,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(F8,F6,G5)));
    }
    @Test
    public void getKnightMoves_CaptureOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Knight testedPiece = new Knight(A8,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C7,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B6, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(1,knightMoves.size());
        assertTrue(knightMoveCoordinate.contains(B6));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getKnightMoves_CaptureOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Knight testedPiece = new Knight(G7,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E8,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E6,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(H5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(E6,F5,H5)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getKnightMoves_MixedMoves() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Knight testedPiece = new Knight(B2,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A4,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new King(C4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D3,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> knightMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> knightMoveCoordinate = knightMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = knightMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,knightMoves.size());
        assertTrue(knightMoveCoordinate.containsAll(Set.of(C4,D3,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
        assertTrue(movesTags.contains(ChessMoveTags.ATTACKS_KING));
    }
}
