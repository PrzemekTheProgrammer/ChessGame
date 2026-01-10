package pszerszenowicz.ports.move;

import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move {
    private final Piece piece;
    private final PieceCoordinate from;
    private final PieceCoordinate to;
    private final ArrayList<MoveTags> tags = new ArrayList<>();
    private final Piece auxillaryPiece;

    public Move(Piece piece, PieceCoordinate to, Piece auxillaryPiece) {
        this.from = piece.getPieceCoordinate();
        this.piece = piece;
        this.to = to;
        this.auxillaryPiece = auxillaryPiece;
    }

    public Move(Piece piece, PieceCoordinate to) {
        this(piece, to, null);
    }

    public Piece getAuxillaryPiece() {
        return auxillaryPiece;
    }

    public List<MoveTags> getTags() {
        return tags;
    }

    public void addTag(MoveTags tag) {
        tags.add(tag);
    }

    public boolean hasTag(MoveTags tag) {
        return tags.contains(tag);
    }

    public PieceCoordinate getTo() {
        return to;
    }

    public Piece getPiece() {
        return piece;
    }

    public PieceCoordinate getFrom() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(piece, move.piece) && Objects.equals(from, move.from) && Objects.equals(to, move.to) && Objects.equals(tags, move.tags) && Objects.equals(auxillaryPiece, move.auxillaryPiece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, to, tags, auxillaryPiece);
    }
}
