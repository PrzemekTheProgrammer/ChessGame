package pszerszenowicz.domain.adapters;

import pszerszenowicz.ports.piece.PieceColor;

public class Player {
    final PieceColor pieceColor;

    public Player(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }
}
