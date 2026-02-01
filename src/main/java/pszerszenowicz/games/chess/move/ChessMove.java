package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.domain.ports.game.Move;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.game.MoveTag;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.piece.*;

import java.util.*;

public class ChessMove implements Move {
    private final Piece piece;
    private final PieceCoordinate from;
    private final PieceCoordinate to;
    private final EnumSet<ChessMoveTags> tags = EnumSet.noneOf(ChessMoveTags.class);
    private final Piece auxillaryPiece;

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
    public void addTag(MoveTag tag) {
        if (tag instanceof ChessMoveTags) {
            tags.add((ChessMoveTags) tag);
        }
    }

    @Override
    public boolean hasTag(MoveTag tag) {
        if (tag instanceof ChessMoveTags) {
            return tags.contains(tag);
        }
        return false;
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
    public void apply(Board board) {
        if (board instanceof ChessBoard) {
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
            if(hasTag(ChessMoveTags.PROMOTE_BISHOP)) {
                board.addPiece(new Bishop(piece.getPieceCoordinate(), piece.getColor()));
            }
            if(hasTag(ChessMoveTags.PROMOTE_KNIGHT)) {
                board.addPiece(new Knight(piece.getPieceCoordinate(), piece.getColor()));
            }
            if(hasTag(ChessMoveTags.PROMOTE_ROOK)) {
                board.addPiece(new Rook(piece.getPieceCoordinate(), piece.getColor()));
            }
            if(hasTag(ChessMoveTags.PROMOTE_QUEEN)) {
                board.addPiece(new Queen(piece.getPieceCoordinate(), piece.getColor()));
            }
            if (piece instanceof King king) {
                king.loseCastleRight();
            }
            if (piece instanceof Rook rook) {
                rook.loseCastleRight();
            }
        }
    }

    @Override
    public void undo(Board board) {
        if (board instanceof ChessBoard) {
            board.removePiece(to);
            piece.setPieceCoordinate(from);
            board.addPiece(piece);
            if (hasTag(ChessMoveTags.Capture)) {
                board.addPiece(auxillaryPiece);
            }
            if (hasTag(ChessMoveTags.Castle)) {
                board.removePiece(auxillaryPiece.getPieceCoordinate());
                int rookColumn = to.getColumn() > from.getColumn() ? 8 : 1;
                auxillaryPiece.setPieceCoordinate(ChessBoard.getCoordinate(
                        rookColumn,
                        auxillaryPiece.getPieceCoordinate().getRow()
                ));
                board.addPiece(auxillaryPiece);
            }
            if (piece instanceof King king) {
                king.applyCastleRight();
            }
            if (piece instanceof Rook rook) {
                rook.applyCastleRight();
            }
        }
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
