package pszerszenowicz.games.chess.game;

import org.junit.jupiter.api.Test;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.piece.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pszerszenowicz.games.chess.board.ChessBoard.*;

public class ChessRulesTest {

    @Test
    public void getLegalMovesTest_KingChecked01() {
        //given
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King testedPiece = new King(E1, board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Rook(A1, board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(F1, board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(E6, board.black());
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King testedPiece = new King(E1, board.white());
        board.addPiece(testedPiece);
        Rook testedPiece2 = new Rook(H3, board.white());
        board.addPiece(testedPiece2);
        Piece tmpPiece = new Rook(A1, board.white());
        board.addPiece(tmpPiece);
        tmpPiece = new Rook(E6, board.black());
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        Pawn testedPiece = new Pawn(F5, board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E5, board.black());
        board.addPiece(tmpPiece);
        ChessMove move = new ChessMove(tmpPiece,E5);
        move.addTag(ChessMoveTags.Charge);
        history.add(move);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        Pawn testedPiece = new Pawn(F5, board.white());
        board.addPiece(testedPiece);
        Piece tmpPiece = new Pawn(E5, board.black());
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King testedPiece = new King(E1, board.white());
        board.addPiece(testedPiece);
        Rook testedPiece2 = new Rook(H1, board.white());
        board.addPiece(testedPiece2);
        Rook testedPiece3 = new Rook(A1, board.white());
        board.addPiece(testedPiece3);
        Piece tmpPiece = new Rook(F6, board.black());
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King testedPiece = new King(H3, board.white());
        board.addPiece(testedPiece);
        Queen testedPiece2 = new Queen(G4, board.white());
        board.addPiece(testedPiece2);
        Bishop tmpPiece = new Bishop(D7, board.black());
        board.addPiece(tmpPiece);
        //when
        Set<ChessMove> avaibleMoves = chessRules.legalMoves(board, board.white(), context);
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
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King piece1 = new King(H3, board.white());
        board.addPiece(piece1);
        Queen piece2 = new Queen(G4, board.black());
        board.addPiece(piece2);
        //when
        ChessGameStatus status = (ChessGameStatus) chessRules.evaluateGameState(
                chessRules.legalMoves(board, PieceColor.WHITE,context),
                board,
                PieceColor.WHITE,
                history
        );
        //then
        assertEquals(ChessGameStatus.ONGOING, status);
    }

    @Test
    void getGameStatus_Stalemate() {
        //given
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King piece1 = new King(A1, board.white());
        board.addPiece(piece1);
        Queen piece2 = new Queen(B3, board.black());
        board.addPiece(piece2);
        //when
        ChessGameStatus status = (ChessGameStatus) chessRules.evaluateGameState(
                chessRules.legalMoves(board, PieceColor.WHITE,context),
                board,
                PieceColor.WHITE,
                history
        );
        //then
        assertEquals(ChessGameStatus.STALEMATE, status);
    }

    @Test
    void getGameStatus_Black_Win() {
        //given
        ChessRules chessRules = new ChessRules();
        Board board = new ChessBoard();
        List<ChessMove> history = new ArrayList<>();
        ChessContext context = new ChessContext(history);
        King piece1 = new King(A1, board.white());
        board.addPiece(piece1);
        Queen piece2 = new Queen(B2, board.black());
        board.addPiece(piece2);
        Queen piece3 = new Queen(A2, board.black());
        board.addPiece(piece3);
        //when
        ChessGameStatus status = (ChessGameStatus) chessRules.evaluateGameState(
                chessRules.legalMoves(board, PieceColor.WHITE,context),
                board,
                PieceColor.WHITE,
                history
        );
        //then
        assertEquals(ChessGameStatus.BLACK_WIN, status);
    }

}
