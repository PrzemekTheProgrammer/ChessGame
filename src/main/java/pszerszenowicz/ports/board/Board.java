package pszerszenowicz.ports.board;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceColor;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.*;

public abstract class Board {
    private Player white = new Player(PieceColor.WHITE);
    private Player black = new Player(PieceColor.BLACK);
    private Map<PieceCoordinate,Piece> pieceCoordinate = new HashMap<>();
    private List<Piece> pieces = new ArrayList<>();

    public abstract void setBoard();

    public void addPiece(Piece piece) {
        pieces.add(piece);
        pieceCoordinate.put(piece.getPieceCoordinate(),piece);
    }
    public Set<Move> avaibleMoves(Player player){
        if(player != white && player !=black){
            //TODO throw
        }
        Set<Move> ret = new HashSet<>();
        List<Piece> pieces = this.pieces.stream().filter((piece) ->
        {
            return piece.getPlayer() == player;
        })

                .toList();
        for (Piece piece : pieces) {
            ret.addAll(piece.getMoves(this));
        }
        return ret;
    }

    public Map<PieceCoordinate, Piece> getPieceCoordinate() {
        return pieceCoordinate;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }
}
