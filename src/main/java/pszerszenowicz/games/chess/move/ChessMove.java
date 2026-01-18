package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.ports.Move;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.domain.ports.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessMove implements Move {
    private final Piece piece;
    private final PieceCoordinate from;
    private final PieceCoordinate to;
    private final ArrayList<ChessMoveTags> tags = new ArrayList<>();
    private final Piece auxillaryPiece;

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

    public List<ChessMoveTags> getTags() {
        return tags;
    }

    @Override
    public void addTag(Tag tag) {
        if (tag instanceof ChessMoveTags) {
            tags.add((ChessMoveTags)tag);
        }
    }

    @Override
    public boolean hasTag(Tag tag) {
        if(tag instanceof ChessMoveTags) {
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
