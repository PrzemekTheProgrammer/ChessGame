package pszerszenowicz.games.chess.position;

public final class CastlingRights {

    public static final int WHITE_KING_SIDE  = 1;  // 0001
    public static final int WHITE_QUEEN_SIDE = 2;  // 0010
    public static final int BLACK_KING_SIDE  = 4;  // 0100
    public static final int BLACK_QUEEN_SIDE = 8;  // 1000

    private int mask;

    private CastlingRights(int mask) {
        this.mask = mask;
    }

    public CastlingRights(ChessPosition position) {
        this.mask = position.getCastlingRights().mask;
    }

    public CastlingRights(ChessPositionSnapshot chessPositionSnapshot) {
        this.mask = chessPositionSnapshot.getCastlingRights().mask;
    }

    public static CastlingRights initial() {
        return new CastlingRights(
                WHITE_KING_SIDE |
                        WHITE_QUEEN_SIDE |
                        BLACK_KING_SIDE |
                        BLACK_QUEEN_SIDE
        );
    }

    public boolean hasRight(int right) {
        return (mask & right) != 0;
    }

    public void disable(int right) {
        mask &= ~right;
    }

    public void enable(int right) {
        this.mask |= right;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

}
