package pszerszenowicz.games.chess.game;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.piece.*;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class ChessRulesTest {

    @Test
    public void getLegalMovesTest_KingChecked01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King testedPiece = new King(E1, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1, PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(F1, PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(E6, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(3, avaibleMoves.size());
        assertEquals(Set.of(D1, D2, F2), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(D1).contains(testedPiece));
        assertTrue(pieceLegalMove.get(D2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(F2).contains(testedPiece));
    }

    @Test
    public void getLegalMovesTest_KingChecked02() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King testedPiece = new King(E1, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Rook testedPiece2 = new Rook(H3, PieceColor.WHITE);
        board.addPiece(testedPiece2);
        Piece tmpPiece = new Rook(A1, PieceColor.WHITE);
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(E6, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(5, avaibleMoves.size());
        assertEquals(Set.of(D1, D2, F1, F2, E3), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(D1).contains(testedPiece));
        assertTrue(pieceLegalMove.get(D2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(F1).contains(testedPiece));
        assertTrue(pieceLegalMove.get(F2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(E3).contains(testedPiece2));
    }

    @Test
    public void getLegalMovesTest_EnPassant01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        Pawn testedPiece = new Pawn(F5, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E5, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        position.setEnPassantSquare(E6);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(2, avaibleMoves.size());
        assertEquals(Set.of(F6, E6), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(F6).contains(testedPiece));
        assertTrue(pieceLegalMove.get(E6).contains(testedPiece));
    }

    @Test
    public void getLegalMovesTest_EnPassantNotAvailable01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        Pawn testedPiece = new Pawn(F5, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E5, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(1, avaibleMoves.size());
        assertEquals(Set.of(F6), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(F6).contains(testedPiece));
    }

    @Test
    public void getLegalMovesTest_Castle01() {

        /*
        W tym teście:
        Roszada na C1 - dostępna
        Roszada na G1 - niedostępna
        */
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King testedPiece = new King(E1, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Rook testedPiece2 = new Rook(H1, PieceColor.WHITE);
        board.addPiece(testedPiece2);
        Rook testedPiece3 = new Rook(A1, PieceColor.WHITE);
        board.addPiece(testedPiece3);
        Piece tmpPiece = new Rook(F6, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(23, avaibleMoves.size());
        assertEquals(Set.of(A2,A3,A4,A5,A6,A7,A8,B1,C1,D1,
                H2,H3,H4,H5,H6,H7,H8,G1,F1,
                D2,E2), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(D1).contains(testedPiece));
        assertTrue(pieceLegalMove.get(D2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(E2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(C1).contains(testedPiece));
        assertTrue(pieceLegalMove.get(H2).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H3).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H4).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H5).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H6).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H7).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(H8).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(G1).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(F1).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(A2).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A3).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A4).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A5).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A6).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A7).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(A8).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(B1).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(C1).contains(testedPiece3));
        assertTrue(pieceLegalMove.get(D1).contains(testedPiece3));
    }

    @Test
    public void getLegalMovesTest_FigurePinned01() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King testedPiece = new King(H3, PieceColor.WHITE);
        board.addPiece(testedPiece);
        Queen testedPiece2 = new Queen(G4, PieceColor.WHITE);
        board.addPiece(testedPiece2);
        Bishop tmpPiece = new Bishop(D7, PieceColor.BLACK);
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = position.legalMoves();
        Map<PieceCoordinate, Set<Piece>> pieceLegalMove = avaibleMoves.stream()
                .collect(Collectors
                        .groupingBy(
                                ChessMove::to,
                                Collectors.mapping(
                                        ChessMove::piece,
                                        Collectors.toSet())));
        //then
        assertEquals(7, avaibleMoves.size());
        assertEquals(Set.of(F5,E6,D7,H4,G3,G2,H2), pieceLegalMove.keySet());
        assertTrue(pieceLegalMove.get(H4).contains(testedPiece));
        assertTrue(pieceLegalMove.get(G3).contains(testedPiece));
        assertTrue(pieceLegalMove.get(G2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(H2).contains(testedPiece));
        assertTrue(pieceLegalMove.get(F5).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(E6).contains(testedPiece2));
        assertTrue(pieceLegalMove.get(D7).contains(testedPiece2));
    }

    @Test
    void getGameStatus_Ongoing() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King piece1 = new King(H3, PieceColor.WHITE);
        board.addPiece(piece1);
        Queen piece2 = new Queen(G4, PieceColor.BLACK);
        board.addPiece(piece2);
        //when
        GameStatus status = position.evaluateGameState();
        //then
        assertEquals(GameStatus.ONGOING, status);
    }

    @Test
    void getGameStatus_Stalemate() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King piece1 = new King(A1, PieceColor.WHITE);
        board.addPiece(piece1);
        Queen piece2 = new Queen(B3, PieceColor.BLACK);
        board.addPiece(piece2);
        //when
        GameStatus status = position.evaluateGameState();
        //then
        assertEquals(GameStatus.STALEMATE, status);
    }

    @Test
    void getGameStatus_Black_Win() {
        //given
        ChessBoard board = new ChessBoard();
        ChessPosition position = new ChessPosition(board);
        King piece1 = new King(A1, PieceColor.WHITE);
        board.addPiece(piece1);
        Queen piece2 = new Queen(B2, PieceColor.BLACK);
        board.addPiece(piece2);
        Queen piece3 = new Queen(A2, PieceColor.BLACK);
        board.addPiece(piece3);
        //when
        GameStatus status = position.evaluateGameState();
        //then
        assertEquals(GameStatus.BLACK_WIN, status);
    }

}
