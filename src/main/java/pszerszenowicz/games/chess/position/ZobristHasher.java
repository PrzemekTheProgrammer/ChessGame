package pszerszenowicz.games.chess.position;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.piece.*;

import java.util.Random;

public final class ZobristHasher {

    private static final long[][][] PIECES = new long[2][6][64];

    private static final long SIDE_TO_MOVE;

    private static final long[] CASTLING_RIGHTS = new long[16];

    private static final long[] EN_PASSANT_FILE = new long[8];

    private static final long[] HALF_MOVE_CLOCK = new long[101];

    static {
        Random random = new Random(123456789L);

        for (int color = 0; color < 2; color++) {
            for (int piece = 0; piece < 6; piece++) {
                for (int square = 0; square < 64; square++) {
                    PIECES[color][piece][square] = random.nextLong();
                }
            }
        }

        SIDE_TO_MOVE = random.nextLong();

        for (int i = 0; i < 16; i++) {
            CASTLING_RIGHTS[i] = random.nextLong();
        }

        for (int i = 0; i < 8; i++) {
            EN_PASSANT_FILE[i] = random.nextLong();
        }

        for (int i = 0; i < HALF_MOVE_CLOCK.length; i++) {
            HALF_MOVE_CLOCK[i] = random.nextLong();
        }
    }

    private ZobristHasher() {
    }

    public static long hash(ChessPosition position) {

        long hash = repetitionHash(position);

        int halfMoveClock = Math.min(
                position.getHalfMoveClock(),
                HALF_MOVE_CLOCK.length - 1
        );

        hash ^= HALF_MOVE_CLOCK[halfMoveClock];

        return hash;
    }

    public static long repetitionHash(ChessPosition position) {
        long hash = 0;

        for (Piece piece : position.getChessBoard().pieces()) {

            int color =
                    piece.getColor() == PieceColor.WHITE
                            ? 0
                            : 1;

            int type = pieceType(piece);

            int square = square(piece.getPieceCoordinate());

            hash ^= PIECES[color][type][square];
        }

        if (position.getSideToMove() == PieceColor.BLACK) {
            hash ^= SIDE_TO_MOVE;
        }

        hash ^= CASTLING_RIGHTS[
                position.getCastlingRights().getMask()
                ];

        PieceCoordinate enPassantSquare =
                position.getEnPassantSquare();

        if (enPassantSquare != null) {

            int file =
                    enPassantSquare.getColumn() - 1;

            hash ^= EN_PASSANT_FILE[file];
        }

        return hash;
    }

    private static int square(PieceCoordinate coordinate) {

        int column = coordinate.getColumn() - 1;
        int row = coordinate.getRow() - 1;

        return row * 8 + column;
    }

    private static int pieceType(Piece piece) {

        if (piece instanceof Pawn) {
            return 0;
        }

        if (piece instanceof Knight) {
            return 1;
        }

        if (piece instanceof Bishop) {
            return 2;
        }

        if (piece instanceof Rook) {
            return 3;
        }

        if (piece instanceof Queen) {
            return 4;
        }

        if (piece instanceof King) {
            return 5;
        }

        throw new IllegalArgumentException(
                "Unknown piece type: " + piece.getClass()
        );
    }
}