package pszerszenowicz.games.chess.position;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

public class ChessPositionSnapshot {
    private final PieceColor sideToMove;
    private final CastlingRights castlingRights;
    private final PieceCoordinate enPassantSquare;
    private final int halfMoveClock;

    public ChessPositionSnapshot(ChessPosition chessPosition) {
        this.sideToMove = chessPosition.getSideToMove();
        this.castlingRights = new CastlingRights(chessPosition);
        this.enPassantSquare = chessPosition.getEnPassantSquare();
        this.halfMoveClock = chessPosition.getHalfMoveClock();
    }

    public PieceColor getSideToMove() {
        return sideToMove;
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public PieceCoordinate getEnPassantSquare() {
        return enPassantSquare;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }
}
