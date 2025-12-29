package pszerszenowicz.chess.adapters.board;

import pszerszenowicz.chess.adapters.piece.*;
import pszerszenowicz.ports.board.*;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ChessBoard extends Board {
    
    public static final PieceCoordinate A1 = new PieceCoordinate(1, 1);
    public static final PieceCoordinate A2 = new PieceCoordinate(1, 2);
    public static final PieceCoordinate A3 = new PieceCoordinate(1, 3);
    public static final PieceCoordinate A4 = new PieceCoordinate(1, 4);
    public static final PieceCoordinate A5 = new PieceCoordinate(1, 5);
    public static final PieceCoordinate A6 = new PieceCoordinate(1, 6);
    public static final PieceCoordinate A7 = new PieceCoordinate(1, 7);
    public static final PieceCoordinate A8 = new PieceCoordinate(1, 8);

    public static final PieceCoordinate B1 = new PieceCoordinate(2, 1);
    public static final PieceCoordinate B2 = new PieceCoordinate(2, 2);
    public static final PieceCoordinate B3 = new PieceCoordinate(2, 3);
    public static final PieceCoordinate B4 = new PieceCoordinate(2, 4);
    public static final PieceCoordinate B5 = new PieceCoordinate(2, 5);
    public static final PieceCoordinate B6 = new PieceCoordinate(2, 6);
    public static final PieceCoordinate B7 = new PieceCoordinate(2, 7);
    public static final PieceCoordinate B8 = new PieceCoordinate(2, 8);

    public static final PieceCoordinate C1 = new PieceCoordinate(3, 1);
    public static final PieceCoordinate C2 = new PieceCoordinate(3, 2);
    public static final PieceCoordinate C3 = new PieceCoordinate(3, 3);
    public static final PieceCoordinate C4 = new PieceCoordinate(3, 4);
    public static final PieceCoordinate C5 = new PieceCoordinate(3, 5);
    public static final PieceCoordinate C6 = new PieceCoordinate(3, 6);
    public static final PieceCoordinate C7 = new PieceCoordinate(3, 7);
    public static final PieceCoordinate C8 = new PieceCoordinate(3, 8);

    public static final PieceCoordinate D1 = new PieceCoordinate(4, 1);
    public static final PieceCoordinate D2 = new PieceCoordinate(4, 2);
    public static final PieceCoordinate D3 = new PieceCoordinate(4, 3);
    public static final PieceCoordinate D4 = new PieceCoordinate(4, 4);
    public static final PieceCoordinate D5 = new PieceCoordinate(4, 5);
    public static final PieceCoordinate D6 = new PieceCoordinate(4, 6);
    public static final PieceCoordinate D7 = new PieceCoordinate(4, 7);
    public static final PieceCoordinate D8 = new PieceCoordinate(4, 8);

    public static final PieceCoordinate E1 = new PieceCoordinate(5, 1);
    public static final PieceCoordinate E2 = new PieceCoordinate(5, 2);
    public static final PieceCoordinate E3 = new PieceCoordinate(5, 3);
    public static final PieceCoordinate E4 = new PieceCoordinate(5, 4);
    public static final PieceCoordinate E5 = new PieceCoordinate(5, 5);
    public static final PieceCoordinate E6 = new PieceCoordinate(5, 6);
    public static final PieceCoordinate E7 = new PieceCoordinate(5, 7);
    public static final PieceCoordinate E8 = new PieceCoordinate(5, 8);

    public static final PieceCoordinate F1 = new PieceCoordinate(6, 1);
    public static final PieceCoordinate F2 = new PieceCoordinate(6, 2);
    public static final PieceCoordinate F3 = new PieceCoordinate(6, 3);
    public static final PieceCoordinate F4 = new PieceCoordinate(6, 4);
    public static final PieceCoordinate F5 = new PieceCoordinate(6, 5);
    public static final PieceCoordinate F6 = new PieceCoordinate(6, 6);
    public static final PieceCoordinate F7 = new PieceCoordinate(6, 7);
    public static final PieceCoordinate F8 = new PieceCoordinate(6, 8);

    public static final PieceCoordinate G1 = new PieceCoordinate(7, 1);
    public static final PieceCoordinate G2 = new PieceCoordinate(7, 2);
    public static final PieceCoordinate G3 = new PieceCoordinate(7, 3);
    public static final PieceCoordinate G4 = new PieceCoordinate(7, 4);
    public static final PieceCoordinate G5 = new PieceCoordinate(7, 5);
    public static final PieceCoordinate G6 = new PieceCoordinate(7, 6);
    public static final PieceCoordinate G7 = new PieceCoordinate(7, 7);
    public static final PieceCoordinate G8 = new PieceCoordinate(7, 8);

    public static final PieceCoordinate H1 = new PieceCoordinate(8, 1);
    public static final PieceCoordinate H2 = new PieceCoordinate(8, 2);
    public static final PieceCoordinate H3 = new PieceCoordinate(8, 3);
    public static final PieceCoordinate H4 = new PieceCoordinate(8, 4);
    public static final PieceCoordinate H5 = new PieceCoordinate(8, 5);
    public static final PieceCoordinate H6 = new PieceCoordinate(8, 6);
    public static final PieceCoordinate H7 = new PieceCoordinate(8, 7);
    public static final PieceCoordinate H8 = new PieceCoordinate(8, 8);

    private static final Set<PieceCoordinate> pieceCoordinates = Set.of(A1,A2,A3,A4,A5,A6,A7,A8,
            B1,B2,B3,B4,B5,B6,B7,B8,
            C1,C2,C3,C4,C5,C6,C7,C8,
            D1,D2,D3,D4,D5,D6,D7,D8,
            E1,E2,E3,E4,E5,E6,E7,E8,
            F1,F2,F3,F4,F5,F6,F7,F8,
            G1,G2,G3,G4,G5,G6,G7,G8,
            H1,H2,H3,H4,H5,H6,H7,H8);

    private static final Map<String,PieceCoordinate> lookup = new HashMap<>();

    static {
        for(PieceCoordinate pc : pieceCoordinates) {
            lookup.put(getCoordinateAsString(pc.getColumn(),pc.getRow()),pc);
        }
    }

    public static PieceCoordinate getCoordinate(int horVal, int verVal) {
        return lookup.get(getCoordinateAsString(horVal,verVal));
    }

    private static String getCoordinateAsString(int horVal, int verVal) {
        return "" + (char) ('A' + horVal-1) + verVal;
    }

    @Override
    public void setBoard() {
        PieceCoordinate tmp;
        Piece piece;
        for(int col = 1; col <=8 ; col++){
            tmp = getCoordinate(col,2);
            piece = new Pawn(tmp,this.getWhite());
            addPiece(piece);
        }
        for(int col = 1; col <=8 ; col++){
            tmp = getCoordinate(col,7);
            piece = new Pawn(tmp,this.getBlack());
            addPiece(piece);
        }

        piece = new Rook(A1,this.getWhite());
        addPiece(piece);
        piece = new Rook(H1,this.getWhite());
        addPiece(piece);

        piece = new Rook(A8,this.getBlack());
        addPiece(piece);
        piece = new Rook(H8,this.getBlack());
        addPiece(piece);

        piece = new Knight(B1,this.getWhite());
        addPiece(piece);
        piece = new Knight(G1,this.getWhite());
        addPiece(piece);

        piece = new Knight(B8,this.getBlack());
        addPiece(piece);
        piece = new Knight(G8,this.getBlack());
        addPiece(piece);

        piece = new Bishop(C1,this.getWhite());
        addPiece(piece);
        piece = new Bishop(F1,this.getWhite());
        addPiece(piece);

        piece = new Bishop(C8,this.getBlack());
        addPiece(piece);
        piece = new Bishop(F8,this.getBlack());
        addPiece(piece);

        piece = new Queen(D1,this.getWhite());
        addPiece(piece);
        piece = new Queen(D8,this.getBlack());
        addPiece(piece);

        piece = new King(E1,this.getWhite());
        addPiece(piece);
        piece = new King(E8,this.getBlack());
        addPiece(piece);
    }



}
