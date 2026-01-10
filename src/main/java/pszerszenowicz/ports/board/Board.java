package pszerszenowicz.ports.board;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceColor;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.*;

public abstract class Board {
    private final Player white;
    private final Player black;
    private final Map<PieceCoordinate, Piece> pieceCoordinate = new HashMap<>();
    private final List<Piece> pieces = new ArrayList<>();

    public Board(Board board) {
        white = board.getWhite();
        black = board.getBlack();
        this.pieces.addAll(board.pieces);
        pieceCoordinate.putAll(board.pieceCoordinate);
    }

    public Board(){
        white = new Player(PieceColor.WHITE);
        black = new Player(PieceColor.BLACK);
    };

    public abstract void setBoard();

    public void addPiece(Piece piece) {
        pieces.add(piece);
        pieceCoordinate.put(piece.getPieceCoordinate(), piece);
    }

    public Set<Move> avaibleMoves(Player player) {
        Set<Move> ret = new HashSet<>();
        List<Piece> pieces = this.pieces.stream().filter((piece) -> piece.getPlayer() == player).toList();
        for (Piece piece : pieces) {
            ret.addAll(piece.getMoves(this));
        }
        return ret;
    }

    public Map<PieceCoordinate, Piece> getPieceCoordinate() {
        return pieceCoordinate;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }

    public abstract void move(Move move);
    public abstract void undoMove(Move move);
}
