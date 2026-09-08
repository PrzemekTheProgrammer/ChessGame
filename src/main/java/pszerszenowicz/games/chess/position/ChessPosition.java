package pszerszenowicz.games.chess.position;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Position;
import pszerszenowicz.games.chess.game.GameStatus;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;

import java.util.Set;
import java.util.stream.Collectors;

public class ChessPosition implements Position {
    private ChessBoard chessBoard;
    private PieceColor sideToMove;
    private CastlingRights castlingRights;
    private PieceCoordinate enPassantSquare;
    private int halfMoveClock;

    public ChessPosition(ChessBoard board) {
        this.chessBoard = board;
        this.sideToMove = PieceColor.WHITE;
        this.castlingRights = CastlingRights.initial();
        this.enPassantSquare = null;
        this.halfMoveClock = 0;
    }

    public void updateFromSnapshot(ChessPositionSnapshot chessPositionSnapshot) {
        this.sideToMove = chessPositionSnapshot.getSideToMove();
        this.castlingRights = new CastlingRights(chessPositionSnapshot);
        this.enPassantSquare = chessPositionSnapshot.getEnPassantSquare();
        this.halfMoveClock = chessPositionSnapshot.getHalfMoveClock();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public PieceColor getSideToMove() {
        return sideToMove;
    }

    public void oppositeSideToMove() {
        sideToMove = sideToMove == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }

    public void setSideToMove(PieceColor sideToMove) {
        this.sideToMove = sideToMove;
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public void setCastlingRights(CastlingRights castlingRights) {
        this.castlingRights = castlingRights;
    }

    public PieceCoordinate getEnPassantSquare() {
        return enPassantSquare;
    }

    public void setEnPassantSquare(PieceCoordinate enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    @Override
    public Set<ChessMove> legalMoves() {
        return availableMoves().stream()
                .filter(move -> isMoveLegal(move))
                .collect(Collectors.toSet());
    }

    @Override
    public GameStatus evaluateGameState() {
        Set<? extends Move> legalMoves = legalMoves();
        if (legalMoves.isEmpty()) {
            PieceColor currentPlayer = getSideToMove();
            oppositeSideToMove();
            boolean kingAttacked = availableMoves().stream().anyMatch(move -> move.hasTag(ChessMoveTags.AttacksKing));
            oppositeSideToMove();
            if (kingAttacked) {
                if(currentPlayer== PieceColor.WHITE) {
                    return GameStatus.BLACK_WIN;
                }else {
                    return GameStatus.WHITE_WIN;
                }
            }
            return GameStatus.STALEMATE;
        }
        if (getHalfMoveClock() >= 50) {
            return GameStatus.STALEMATE;
        }
        return GameStatus.ONGOING;
    }

    @Override
    public Set<ChessMove> availableMoves() {
        return chessBoard.pieces().stream()
                .filter(piece -> piece.getColor() == sideToMove)
                .flatMap(piece -> piece.getMoves(this).stream())
                .map(ChessMove.class::cast)
                .collect(Collectors.toSet());
    }

    private boolean isMoveLegal(ChessMove move) {
        move.apply(this);
        Set<ChessMove> opponentMoves = availableMoves();
        move.undo(this);
        boolean kingAttacked = opponentMoves.stream()
                .anyMatch(m -> m.getTags().contains(ChessMoveTags.AttacksKing));

        if (kingAttacked) {
            return false;
        }

        oppositeSideToMove();
        opponentMoves = availableMoves();
        oppositeSideToMove();

        if (move.hasTag(ChessMoveTags.Castle) && !isCastleLegal(move, opponentMoves)) {
            return false;
        }

        if (move.hasTag(ChessMoveTags.EnPassant) && !isEnPassantLegal(move, getEnPassantSquare())) {
            return false;
        }
        return true;
    }

    private boolean isCastleLegal(ChessMove move, Set<ChessMove> opponentMoves) {
        int dir = move.to().getColumn() > move.from().getColumn() ? 1 : -1;
        int row = move.from().getRow();
        for(int passingColumn = move.from().getColumn()+dir; passingColumn != move.to().getColumn() + dir; passingColumn+=dir) {
            final int col = passingColumn;
            if (opponentMoves.stream().anyMatch(m ->
                    (m.to().getColumn() == col && m.to().getRow()==row)
                            || m.hasTag(ChessMoveTags.AttacksKing))){
                return false;
            }
        }
        return true;
    }

    private boolean isEnPassantLegal(ChessMove move, PieceCoordinate enPassantSquare) {
        return enPassantSquare == move.to();
    }
}
