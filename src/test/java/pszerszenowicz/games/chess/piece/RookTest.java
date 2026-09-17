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

public class RookTest {

    @Test
    public void getRookMoves_MoveOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Rook testedPiece = new Rook(D4,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(14,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(A4,B4,C4,E4,F4,G4,H4,D1,D2,D3,D5,D6,D7,D8)));
    }
    @Test
    public void getRookMoves_MoveOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Rook testedPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A4,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(9,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(B1,C1,D1,E1,F1,G1,H1,A2,A3)));
    }
    @Test
    public void getRookMoves_CaptureOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Rook testedPiece = new Rook(D4,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(D3,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D5,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.contains(C4));
        assertTrue(rookMoveCoordinate.contains(E4));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getRookMoves_CaptureOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Rook testedPiece = new Rook(A3,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A2,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(B3,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(A4,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(A4,B3)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getRookMoves_MixedMoves() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        Rook testedPiece = new Rook(H5, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(H7,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new King(D5,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> rookMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> rookMoveCoordinate = rookMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = rookMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(9,rookMoves.size());
        assertTrue(rookMoveCoordinate.containsAll(Set.of(H4,H3,H2,H6,H7,G5,F5,E5,D5)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
        assertTrue(movesTags.contains(ChessMoveTags.ATTACKS_KING));
    }
}
