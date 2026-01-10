package pszerszenowicz.chess.adapters.piece;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import static pszerszenowicz.chess.adapters.board.ChessBoard.*;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    public Knight(PieceCoordinate pieceCoordinate, Player player) {
        super(pieceCoordinate, player);
    }


    @Override
    public Set<Move> getMoves(Board board) {
        Set<Move> possibleMoves = new HashSet<>();
        int[] horizontalDir = {-1, -2, -2, -1, 1, 2, 2, 1};
        int[] verticalDir = {2, 1, -1, -2, -2, -1, 1, 2};
        for (int dir = 0; dir < 8; dir++) {
            int newHorizontalValue = this.getPieceCoordinate().getColumn() + horizontalDir[dir];
            int newVerticalValue = this.getPieceCoordinate().getRow() + verticalDir[dir];
            if (newHorizontalValue < 1 || newHorizontalValue > 8
                    || newVerticalValue < 1 || newVerticalValue > 8) {
                continue;
            }
            PieceCoordinate newCoord = getCoordinate(
                    newHorizontalValue,
                    newVerticalValue);

            Piece existingPiece = board.getPieceCoordinate().get(newCoord);
            if (existingPiece == null) {
                possibleMoves.add(new Move(this,newCoord));
            } else {
                if (existingPiece.getPlayer() != this.getPlayer()) {
                    Move to;
                    if(existingPiece instanceof King) {
                        to = new Move(this,newCoord);
                        to.addTag(MoveTags.AttacksKing);
                    }
                    else{
                        to = new Move(this,newCoord,existingPiece);
                        to.addTag(MoveTags.Capture);
                    }
                    possibleMoves.add(to); // biciecie
                }
            }
        }
        return possibleMoves;
    }

}
