package pszerszenowicz.domain.core.piece;

import java.util.*;

public class PieceCoordinate {

    private final int column;
    private final int row;

    public PieceCoordinate(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceCoordinate that = (PieceCoordinate) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "column=" + column +
                ", row=" + row;
    }
}
