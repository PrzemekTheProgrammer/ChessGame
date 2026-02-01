package pszerszenowicz.games.chess.piece;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.board.ChessBoard;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pszerszenowicz.games.chess.board.ChessBoard.*;

public class BishopTest {

    @Test
    public void getBishopMoves_MoveOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Bishop testedPiece = new Bishop(A1,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(7,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(B2,C3,D4,E5,F6,G7,H8)));
    }

    @Test
    public void getBishopMoves_MoveOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Bishop testedPiece = new Bishop(D4,board.white());
        board.addPiece(testedPiece);
        Set<ChessMove> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(13,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(A7,B6,C5,E3,F2,G1,A1,B2,C3,E5,F6,G7,H8)));
    }
    @Test
    public void getBishopMoves_CaptureOnly1() {
        //given
        Board board = new ChessBoard();
        //when
        Bishop testedPiece = new Bishop(D4,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.contains(E3));
        assertTrue(bishopMoveCoordinate.contains(E5));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }
    @Test
    public void getBishopMoves_CaptureOnly2() {
        //given
        Board board = new ChessBoard();
        //when
        Bishop testedPiece = new Bishop(D4,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(C3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E5,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.contains(E3));
        assertTrue(bishopMoveCoordinate.contains(E5));
        assertTrue(bishopMoveCoordinate.contains(C3));
        assertTrue(bishopMoveCoordinate.contains(C5));
        assertTrue(movesTags.contains(ChessMoveTags.Capture));
    }
    @Test
    public void getBishopMoves_MixedMoves() {
        //given
        Board board = new ChessBoard();
        //when
        Bishop testedPiece = new Bishop(D4,board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(B2,board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(C5,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E3,board.black());
        board.addPiece(tmpPiece);
        tmpPiece = new King(F6,board.black());
        board.addPiece(tmpPiece);
        Set<ChessMove> bishopMoves = testedPiece.getMoves(board);
        List<PieceCoordinate> bishopMoveCoordinate = bishopMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = bishopMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,bishopMoves.size());
        assertTrue(bishopMoveCoordinate.containsAll(Set.of(C3,C5,E3,E5,F6)));
        assertTrue(movesTags.containsAll(Set.of(ChessMoveTags.Capture, ChessMoveTags.AttacksKing)));
    }

}
