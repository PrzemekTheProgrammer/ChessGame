package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.games.chess.piece.*;
import pszerszenowicz.games.chess.position.CastlingRights;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;
import pszerszenowicz.games.chess.position.ChessPositionSnapshot;

import java.util.EnumSet;
import java.util.Objects;

import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class ChessMove implements Move {
    private final Piece piece;
    private final PieceCoordinate from;
    private final PieceCoordinate to;
    private final EnumSet<ChessMoveTags> tags = EnumSet.noneOf(ChessMoveTags.class);
    private final Piece auxillaryPiece;
    private ChessPositionSnapshot chessPositionSnapshot;

    public ChessMove(ChessMove source, ChessMoveTags additionalTag) {
        this.piece = source.piece;
        this.from = source.from;
        this.to = source.to;
        this.auxillaryPiece = source.auxillaryPiece;
        this.tags.addAll(source.tags);
        this.tags.add(additionalTag);
    }

    public ChessMove(Piece piece, PieceCoordinate to, Piece auxillaryPiece) {
        this.from = piece.getPieceCoordinate();
        this.piece = piece;
        this.to = to;
        this.auxillaryPiece = auxillaryPiece;
    }

    public ChessMove(Piece piece, PieceCoordinate to) {
        this(piece, to, null);
    }

    public Piece getAuxillaryPiece() {
        return auxillaryPiece;
    }

    public EnumSet<ChessMoveTags> getTags() {
        return tags;
    }

    @Override
    public void addTag(ChessMoveTags tag) {
            tags.add((ChessMoveTags) tag);
    }

    @Override
    public boolean hasTag(ChessMoveTags tag) {
            return tags.contains(tag);
    }

    @Override
    public PieceCoordinate to() {
        return to;
    }

    @Override
    public Piece piece() {
        return piece;
    }

    @Override
    public void apply(ChessPosition position) {
        chessPositionSnapshot = new ChessPositionSnapshot(position);
        ChessBoard board = position.getChessBoard();
        board.removePiece(from);
        piece.setPieceCoordinate(to);
        board.addPiece(piece);
        if (hasTag(ChessMoveTags.EnPassant)) {
            board.removePiece(auxillaryPiece.getPieceCoordinate());
        }
        if (hasTag(ChessMoveTags.Castle)) {
            board.removePiece(auxillaryPiece.getPieceCoordinate());
            int direction = to.getColumn() > from.getColumn() ? -1 : 1;
            auxillaryPiece.setPieceCoordinate(ChessBoard.getCoordinate(
                    to.getColumn() + direction,
                    to.getRow()
            ));
            board.addPiece(auxillaryPiece);
        }
        if (hasTag(ChessMoveTags.PROMOTE_BISHOP)) {
            board.addPiece(new Bishop(piece.getPieceCoordinate(), piece.getColor()));
        }
        if (hasTag(ChessMoveTags.PROMOTE_KNIGHT)) {
            board.addPiece(new Knight(piece.getPieceCoordinate(), piece.getColor()));
        }
        if (hasTag(ChessMoveTags.PROMOTE_ROOK)) {
            board.addPiece(new Rook(piece.getPieceCoordinate(), piece.getColor()));
        }
        if (hasTag(ChessMoveTags.PROMOTE_QUEEN)) {
            board.addPiece(new Queen(piece.getPieceCoordinate(), piece.getColor()));
        }
        if (piece instanceof King) {
            if (position.getSideToMove() == PieceColor.WHITE) {
                position.getCastlingRights().disable(CastlingRights.WHITE_KING_SIDE);
                position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
            } else {
                position.getCastlingRights().disable(CastlingRights.BLACK_KING_SIDE);
                position.getCastlingRights().disable(CastlingRights.BLACK_QUEEN_SIDE);
            }
        }
        if (piece instanceof Rook) {
            if (position.getSideToMove() == PieceColor.WHITE) {
                if (this.from == A1) {
                    position.getCastlingRights().disable(CastlingRights.WHITE_QUEEN_SIDE);
                } else if (this.from == H1) {
                    position.getCastlingRights().disable(CastlingRights.WHITE_KING_SIDE);
                }
            } else {
                if (this.from == A8) {
                    position.getCastlingRights().disable(CastlingRights.BLACK_QUEEN_SIDE);
                } else if (this.from == H8) {
                    position.getCastlingRights().disable(CastlingRights.BLACK_KING_SIDE);
                }
            }
        }
        position.oppositeSideToMove();
    }

    @Override
    public void undo(ChessPosition position) {
        ChessBoard board = position.getChessBoard();
        board.removePiece(to);
        piece.setPieceCoordinate(from);
        board.addPiece(piece);
        if (hasTag(ChessMoveTags.Capture)) {
            board.addPiece(auxillaryPiece);
        }
        if (hasTag(ChessMoveTags.Castle)) {
            int rookColumn = to.getColumn() > from.getColumn() ? 8 : 1;
            auxillaryPiece.setPieceCoordinate(ChessBoard.getCoordinate(
                    rookColumn,
                    auxillaryPiece.getPieceCoordinate().getRow()
            ));
        }
        position.updateFromSnapshot(chessPositionSnapshot);
    }


    @Override
    public PieceCoordinate from() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove move = (ChessMove) o;
        return Objects.equals(piece, move.piece) && Objects.equals(from, move.from) && Objects.equals(to, move.to) && Objects.equals(tags, move.tags) && Objects.equals(auxillaryPiece, move.auxillaryPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, to, tags, auxillaryPiece);
    }

}
