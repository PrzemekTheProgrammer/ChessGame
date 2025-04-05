package pszerszenowicz.domain.ports.piece;

import pszerszenowicz.domain.ports.board.HorizontalBoardNotation;
import pszerszenowicz.domain.ports.board.VerticalBoardNotation;

public class PieceCoordinate {

    private VerticalBoardNotation verticalBoardNotation;
    private HorizontalBoardNotation horizontalBoardNotation;


    public PieceCoordinate(HorizontalBoardNotation horizontalBoardNotation, VerticalBoardNotation verticalBoardNotation) {
        this.verticalBoardNotation = verticalBoardNotation;
        this.horizontalBoardNotation = horizontalBoardNotation;
    }

    public HorizontalBoardNotation getHorizontalBoardNotation() {
        return horizontalBoardNotation;
    }

    public void setHorizontalBoardNotation(HorizontalBoardNotation horizontalBoardNotation) {
        this.horizontalBoardNotation = horizontalBoardNotation;
    }

    public VerticalBoardNotation getVerticalBoardNotation() {
        return verticalBoardNotation;
    }

    public void setVerticalBoardNotation(VerticalBoardNotation verticalBoardNotation) {
        this.verticalBoardNotation = verticalBoardNotation;
    }
}
