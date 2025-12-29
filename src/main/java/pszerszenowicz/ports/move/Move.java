package pszerszenowicz.ports.move;

import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move {
    private Piece piece;
    private PieceCoordinate to;
    private ArrayList<MoveTags> tags = new ArrayList<>();


    public Move(Piece piece, PieceCoordinate to) {
        this.piece = piece;
        this.to = to;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(piece, move.piece) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, to);
    }
}
