package pszerszenowicz.chess.adapters.piece;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.chess.adapters.board.ChessBoard.*;

public class Queen extends Piece {
    public Queen(PieceCoordinate pieceCoordinate, Player player) {
        super(pieceCoordinate, player);
    }

    @Override
    public Set<Move> getMoves(Board board) {
        Set<Move> possibleMoves = new HashSet<>();
        int[] horizontalDir = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] verticalDir = {1, 0, -1, -1, -1, 0, 1, 1};
        for (int dir = 0; dir < 8; dir++) {
            int step = 1;
            while (true) {
                int newHorizontalValue = this.getPieceCoordinate().getColumn() + horizontalDir[dir] * step;
                int newVerticalValue = this.getPieceCoordinate().getRow() + verticalDir[dir] * step;

                if (newHorizontalValue < 1 || newHorizontalValue > 8
                        || newVerticalValue < 1 || newVerticalValue > 8) {
                    break;
                }
                PieceCoordinate newCoord = getCoordinate(newHorizontalValue, newVerticalValue);
                Piece existingPiece = board.getPieceCoordinate().get(newCoord);
                if (existingPiece == null) {
                    possibleMoves.add(new Move(this,newCoord));
                } else {
                    if (existingPiece.getPlayer() != this.getPlayer()) {
                        Move to = new Move(this,newCoord);
                        if(existingPiece instanceof King) {
                            to.addTag(MoveTags.AttacksKing);
                        }else {
                            to.addTag(MoveTags.Capture);
                        }
                        possibleMoves.add(to); // bicie
                    }
                    break; //zatrzymuje się na przeszkodzie lub biciu bierki
                }
                step++;
            }
        }
        return possibleMoves;
    }
}
