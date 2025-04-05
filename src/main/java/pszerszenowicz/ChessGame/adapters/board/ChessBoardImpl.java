package pszerszenowicz.ChessGame.adapters.board;

import pszerszenowicz.ChessGame.adapters.pieces.*;
import pszerszenowicz.domain.ports.board.Board;
import pszerszenowicz.domain.ports.piece.PieceCoordinate;

import static pszerszenowicz.domain.ports.board.HorizontalBoardNotation.*;
import static pszerszenowicz.domain.ports.board.VerticalBoardNotation.*;

public class ChessBoardImpl extends Board {



    @Override
    public void setBoard() {
        PieceCoordinate tmp = new PieceCoordinate(A,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(B,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(C,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(D,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(E,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(F,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(G,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(H,TWO);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerOne()));

        tmp = new PieceCoordinate(A,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(B,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(C,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(D,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(E,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(F,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(G,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(H,SEVEN);
        pieces.put(tmp,new Pawn(tmp,this.getPlayerTwo()));

        tmp = new PieceCoordinate(A,ONE);
        pieces.put(tmp,new Rook(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(H,ONE);
        pieces.put(tmp,new Rook(tmp,this.getPlayerOne()));

        tmp = new PieceCoordinate(A,EIGTH);
        pieces.put(tmp,new Rook(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(H,EIGTH);
        pieces.put(tmp,new Rook(tmp,this.getPlayerTwo()));

        tmp = new PieceCoordinate(B,ONE);
        pieces.put(tmp,new Knight(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(G,ONE);
        pieces.put(tmp,new Knight(tmp,this.getPlayerOne()));

        tmp = new PieceCoordinate(B,EIGTH);
        pieces.put(tmp,new Knight(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(G,EIGTH);
        pieces.put(tmp,new Knight(tmp,this.getPlayerTwo()));

        tmp = new PieceCoordinate(C,ONE);
        pieces.put(tmp,new Bishop(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(F,ONE);
        pieces.put(tmp,new Bishop(tmp,this.getPlayerOne()));

        tmp = new PieceCoordinate(C,EIGTH);
        pieces.put(tmp,new Bishop(tmp,this.getPlayerTwo()));
        tmp = new PieceCoordinate(F,EIGTH);
        pieces.put(tmp,new Bishop(tmp,this.getPlayerTwo()));

        tmp = new PieceCoordinate(D,ONE);
        pieces.put(tmp,new Queen(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(E,EIGTH);
        pieces.put(tmp,new Queen(tmp,this.getPlayerTwo()));

        tmp = new PieceCoordinate(E,ONE);
        pieces.put(tmp,new King(tmp,this.getPlayerOne()));
        tmp = new PieceCoordinate(D,ONE);
        pieces.put(tmp,new King(tmp,this.getPlayerTwo()));
    }

}
