package pszerszenowicz.games.chess.piece;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.ports.game.Board;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.games.chess.board.ChessBoard.getCoordinate;

public class King extends Piece {
    private Boolean canCastle = true;

    public King(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    public void loseCastleRight() {
        canCastle = false;
    }

    public void applyCastleRight() {
        canCastle = true;
    }

    @Override
    public Set<ChessMove> getMoves(Board board) {
        Set<ChessMove> possibleMoves = new HashSet<>();
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

            Piece existingPiece = board.getPiece(newCoord);
            if (existingPiece == null) {
                possibleMoves.add(new ChessMove(this, newCoord));
            } else {
                if (existingPiece.getColor() != this.getColor()) {
                    ChessMove to;
                    if (existingPiece instanceof King) {
                        to = new ChessMove(this, newCoord);
                        to.addTag(ChessMoveTags.AttacksKing);
                    } else {
                        to = new ChessMove(this, newCoord,existingPiece);
                        to.addTag(ChessMoveTags.Capture);
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
                    Piece existingPiece = board.getPiece(newCoord);
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
                        ChessMove to = new ChessMove(this, tmp,existingPiece);
                        to.addTag(ChessMoveTags.Castle);
                        possibleMoves.add(to);
                    }
                    continue castleDirection;
                }
            }
        }
        return possibleMoves;
    }
}
