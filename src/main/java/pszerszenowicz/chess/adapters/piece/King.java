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

public class King extends Piece {
    private Boolean canCastle = true;

    public King(PieceCoordinate pieceCoordinate, Player player) {
        super(pieceCoordinate, player);
    }

    public void loseCastleRight() {
        canCastle = false;
    }

    @Override
    public Set<Move> getMoves(Board board) {
        Set<Move> possibleMoves = new HashSet<>();
        PieceCoordinate from = this.getPieceCoordinate();
        int[] horizontalDir = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] verticalDir = {1, 0, -1, -1, -1, 0, 1, 1};
        for (int dir = 0; dir < 8; dir++) {
            int newHorizontalValue = from.getColumn() + horizontalDir[dir];
            int newVerticalValue = from.getRow() + verticalDir[dir];
            if (newHorizontalValue < 1 || newHorizontalValue > 8
                    || newVerticalValue < 1 || newVerticalValue > 8) {
                continue;
            }
            PieceCoordinate newCoord = getCoordinate(
                    newHorizontalValue,
                    newVerticalValue);

            Piece existingPiece = board.getPieceCoordinate().get(newCoord);
            if (existingPiece == null) {
                possibleMoves.add(new Move(this, newCoord));
            } else {
                if (existingPiece.getPlayer() != this.getPlayer()) {
                    Move to = new Move(this, newCoord);
                    if (existingPiece instanceof King) {
                        to.addTag(MoveTags.AttacksKing);
                    } else {
                        to.addTag(MoveTags.Capture);
                    }
                    possibleMoves.add(to); // bicie
                }
            }
        }

        if (canCastle) {
            horizontalDir = new int[]{-1, 1};
            castleDirection:
            for (int dir = 0; dir < 2; dir++) {
                int step = 1;
                while (true) {
                    int newHorizontalValue = from.getColumn() + horizontalDir[dir] * step;
                    if (newHorizontalValue < 1 || newHorizontalValue > 8) {
                        break;
                    }
                    PieceCoordinate newCoord = getCoordinate(
                            newHorizontalValue,
                            from.getRow());
                    Piece existingPiece = board.getPieceCoordinate().get(newCoord);
                    if (newHorizontalValue != 1 && newHorizontalValue != 8) {
                        if (existingPiece == null) {
                            step++;
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (existingPiece instanceof Rook
                            && ((Rook) existingPiece).canCastle()) {
                        int newHorizontalNotationIntValue = from.getColumn() + horizontalDir[dir] * 2;
                        PieceCoordinate tmp = getCoordinate(
                                newHorizontalNotationIntValue,
                                from.getRow()
                        );
                        Move to = new Move(this, tmp);
                        to.addTag(MoveTags.Castle);
                        possibleMoves.add(to);
                    }
                    continue castleDirection;
                }
            }
        }
        return possibleMoves;
    }
}
