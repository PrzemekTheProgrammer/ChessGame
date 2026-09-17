package pszerszenowicz.games.chess.position;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.ports.game.Position;
import pszerszenowicz.games.chess.game.GameStatus;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.piece.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ChessPosition implements Position {
    private ChessBoard chessBoard;
    private PieceColor sideToMove;
    private CastlingRights castlingRights;
    private PieceCoordinate enPassantSquare;
    private int halfMoveClock;
    private static final int[][] KNIGHT_OFFSETS = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            {1, -2},  {1, 2},
            {2, -1},  {2, 1}
    };
    private static final int[][] STRAIGHT_DIRECTIONS = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };
    private static final int[][] DIAGONAL_DIRECTIONS = {
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1}
    };

    public ChessPosition(ChessBoard board) {
        this.chessBoard = board;
        this.sideToMove = PieceColor.WHITE;
        this.castlingRights = CastlingRights.initial();
        this.enPassantSquare = null;
        this.halfMoveClock = 0;
    }

    public ChessPosition(ChessPosition chessPosition) {
        this.chessBoard = new ChessBoard(chessPosition.chessBoard);
        this.sideToMove = chessPosition.sideToMove;
        this.castlingRights = chessPosition.castlingRights;
        this.enPassantSquare = chessPosition.enPassantSquare;
        this.halfMoveClock = chessPosition.halfMoveClock;
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

    @Override
    public PieceColor getSideToMove() {
        return sideToMove;
    }

    @Override
    public Board getBoard() {
        return chessBoard;
    }

    @Override
    public Long zobristHash() {
        return ZobristHasher.hash(this);
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

    public void incrementHalfMoveCLock() {
        halfMoveClock++;
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
            boolean kingAttacked = availableMoves().stream().anyMatch(move -> move.hasTag(ChessMoveTags.ATTACKS_KING));
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
        if (getHalfMoveClock() >= 100) {
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

    public boolean isSquareAttacked(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        return isAttackedByPawn(square, attackerColor)
                || isAttackedByKnight(square, attackerColor)
                || isAttackedDiagonally(square, attackerColor)
                || isAttackedStraight(square, attackerColor)
                || isAttackedByKing(square, attackerColor);
    }

    private boolean isMoveLegal(ChessMove move) {
        PieceColor movingColor = sideToMove;
        PieceColor opponentColor =
                movingColor == PieceColor.WHITE
                        ? PieceColor.BLACK
                        : PieceColor.WHITE;

        if (move.hasTag(ChessMoveTags.CASTLE)
                && !isCastleLegal(move, movingColor)) {
            return false;
        }

        if (move.hasTag(ChessMoveTags.EN_PASSANT)
                && !isEnPassantLegal(move, getEnPassantSquare())) {
            return false;
        }

        move.apply(this);

        try {
            PieceCoordinate kingSquare =
                    findKingSquare(movingColor);

            return !isSquareAttacked(
                    kingSquare,
                    opponentColor
            );
        } finally {
            move.undo(this);
        }
    }

    private boolean isCastleLegal(
            ChessMove move,
            PieceColor movingColor
    ) {
        PieceColor opponentColor =
                movingColor == PieceColor.WHITE
                        ? PieceColor.BLACK
                        : PieceColor.WHITE;

        int row = move.from().getRow();

        int direction =
                move.to().getColumn() > move.from().getColumn()
                        ? 1
                        : -1;

        for (
                int column = move.from().getColumn();
                column != move.to().getColumn() + direction;
                column += direction
        ) {
            PieceCoordinate square =
                    new PieceCoordinate(column, row);

            if (isSquareAttacked(square, opponentColor)) {
                return false;
            }
        }

        return true;
    }

    private boolean isEnPassantLegal(ChessMove move, PieceCoordinate enPassantSquare) {
        return enPassantSquare == move.to();
    }

    private boolean isInsideBoard(int row, int column) {
        return row >= 1 && row <= 8
                && column >= 1 && column <= 8;
    }

    private boolean isAttackedByKnight(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        for (int[] offset : KNIGHT_OFFSETS) {
            int row = square.getRow() + offset[0];
            int column = square.getColumn() + offset[1];

            if (!isInsideBoard(row, column)) {
                continue;
            }

            Piece piece = chessBoard.getPiece(
                    new PieceCoordinate(column, row)
            );

            if (piece instanceof Knight
                    && piece.getColor() == attackerColor) {
                return true;
            }
        }

        return false;
    }

    private boolean isAttackedByKing(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {

                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }

                int row = square.getRow() + rowOffset;
                int column = square.getColumn() + columnOffset;

                if (!isInsideBoard(row, column)) {
                    continue;
                }

                Piece piece = chessBoard.getPiece(
                        new PieceCoordinate(column, row)
                );

                if (piece instanceof King
                        && piece.getColor() == attackerColor) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isAttackedStraight(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        for (int[] direction : STRAIGHT_DIRECTIONS) {

            int row = square.getRow() + direction[0];
            int column = square.getColumn() + direction[1];

            while (isInsideBoard(row, column)) {

                Piece piece = chessBoard.getPiece(
                        new PieceCoordinate(column, row)
                );

                if (piece != null) {
                    if (piece.getColor() == attackerColor
                            && (piece instanceof Rook
                            || piece instanceof Queen)) {
                        return true;
                    }

                    // Pierwsza figura zasłania wszystko dalej.
                    break;
                }

                row += direction[0];
                column += direction[1];
            }
        }

        return false;
    }

    private boolean isAttackedDiagonally(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        for (int[] direction : DIAGONAL_DIRECTIONS) {

            int row = square.getRow() + direction[0];
            int column = square.getColumn() + direction[1];

            while (isInsideBoard(row, column)) {

                Piece piece = chessBoard.getPiece(
                        new PieceCoordinate(column, row)
                );

                if (piece != null) {
                    if (piece.getColor() == attackerColor
                            && (piece instanceof Bishop
                            || piece instanceof Queen)) {
                        return true;
                    }

                    break;
                }

                row += direction[0];
                column += direction[1];
            }
        }

        return false;
    }

    private boolean isAttackedByPawn(
            PieceCoordinate square,
            PieceColor attackerColor
    ) {
        int pawnRow;

        if (attackerColor == PieceColor.WHITE) {
            pawnRow = square.getRow() - 1;
        } else {
            pawnRow = square.getRow() + 1;
        }

        int leftColumn = square.getColumn() - 1;
        int rightColumn = square.getColumn() + 1;

        return isPawnOfColor(pawnRow, leftColumn, attackerColor)
                || isPawnOfColor(pawnRow, rightColumn, attackerColor);
    }

    private boolean isPawnOfColor(
            int row,
            int column,
            PieceColor color
    ) {
        if (!isInsideBoard(row, column)) {
            return false;
        }

        Piece piece = chessBoard.getPiece(
                new PieceCoordinate(column, row)
        );

        return piece instanceof Pawn
                && piece.getColor() == color;
    }

    private PieceCoordinate findKingSquare(PieceColor color) {
        return chessBoard.pieces().stream()
                .filter(piece -> piece instanceof King)
                .filter(piece -> piece.getColor() == color)
                .findFirst()
                .orElseThrow()
                .getPieceCoordinate();
    }

}
