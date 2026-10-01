package pszerszenowicz.games.chess.piece;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.CastlingRights;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class KingTest {

    @Test
    public void getKingMoves_MoveOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(D6,PieceColor.WHITE);
        position.getCastlingRights().disable(CastlingRights.WHITE_KING_SIDE);
        position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
        board.addPiece(testedPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(8,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(C7,D7,E7,C6,E6,C5,D5,E5)));
    }
    @Test
    public void getKingMoves_MoveOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(A8,PieceColor.WHITE);
        position.getCastlingRights().disable(CastlingRights.WHITE_KING_SIDE);
        position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(A7,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(B8,B7)));
    }
    @Test
    public void getKingMoves_CaptureOnly1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(F1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(F2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D1,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(4,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F2,E2,D2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }

    @Test
    public void getKingMoves_CaptureOnly2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(H1,PieceColor.WHITE);
        position.getCastlingRights().disable(CastlingRights.WHITE_KING_SIDE);
        position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(H2,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G1,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(G2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(2,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(G1,G2)));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
    }
    @Test
    public void getKingMoves_Castle1() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,PieceColor.WHITE);
        position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(6,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.CASTLE));
    }
    @Test
    public void getKingMoves_Castle2() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E1,PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(H1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(7,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,E2,F2,D1,C1)));
        assertTrue(movesTags.contains(ChessMoveTags.CASTLE));
    }
    @Test
    public void getKingMoves_MixedMoves() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        //when
        King testedPiece = new King(E1, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(H1,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(E2,PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Pawn(D2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        tmpPiece = new King(F2,PieceColor.BLACK);
        board.addPiece(tmpPiece);
        Set<ChessMove> kingMoves = testedPiece.getMoves(position);
        List<PieceCoordinate> kingMoveCoordinate = kingMoves.stream().map(ChessMove::to).toList();
        List<ChessMoveTags> movesTags = kingMoves.stream().flatMap(move -> move.getTags().stream()).toList();
        //then
        assertEquals(5,kingMoves.size());
        assertTrue(kingMoveCoordinate.containsAll(Set.of(F1,G1,D2,F2,D1)));
        assertTrue(movesTags.contains(ChessMoveTags.CASTLE));
        assertTrue(movesTags.contains(ChessMoveTags.CAPTURE));
        assertTrue(movesTags.contains(ChessMoveTags.ATTACKS_KING));
    }


}
