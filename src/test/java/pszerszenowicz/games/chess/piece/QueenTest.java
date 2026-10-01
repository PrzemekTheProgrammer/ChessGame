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

public class QueenTest {

    @Test
    public void getQueenMoves_MoveOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Queen testedPiece = new Queen(A2, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Set<ChessMove> queenMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> queenMoveCoordinate = queenMoves.stream().map(ChessMove::to).toList();
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Queen testedPiece = new Queen(F5,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F7,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        Set<ChessMove> queenMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> queenMoveCoordinate = queenMoves.stream().map(ChessMove::to).toList();
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
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Queen testedPiece = new Queen(D4,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D3,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(C4));
        assertTrue(rookMoveCoordinate.contains(E4));
        assertTrue(rookMoveCoordinate.contains(C5));
        assertTrue(rookMoveCoordinate.contains(D5));
        assertTrue(rookMoveCoordinate.contains(E5));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getQueenMoves_CaptureOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Queen testedPiece = new Queen(A1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B1,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(3,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(A2));
        assertTrue(rookMoveCoordinate.contains(B1));
        assertTrue(rookMoveCoordinate.contains(B2));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getQueenMoves_MixedMoves() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Queen testedPiece = new Queen(H8,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H7,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new King(G8,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G7,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(G7));
        assertTrue(rookMoveCoordinate.contains(G8));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
        assertTrue(movesTags.contains(ChessMoveTags.ATTACKS_KING));
    }
}
