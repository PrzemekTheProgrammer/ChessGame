package pszerszenowicz.games.chess.piece;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.games.chess.board.ChessBoard.getCoordinate;

public class Rook extends Piece {
    private Boolean canCastle = true;

    public void loseCastleRight() {
        canCastle = false;
    }

    public void applyCastleRight() {
        canCastle = true;
    }

    public Rook(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    @Override
    public Set<ChessMove> getMoves(Board board) {
        Set<ChessMove> possibleMoves = new HashSet<>();
        int[] horizontalDir = {-1, 1, 0, 0};
        int[] verticalDir = {0, 0, -1, 1};
        for (int dir = 0; dir < 4; dir++) {
            int step = 1;
            while (true) {
                int newHorizontalValue = this.getPieceCoordinate().getColumn() + horizontalDir[dir] * step;
                int newVerticalValue = this.getPieceCoordinate().getRow() + verticalDir[dir] * step;

                if (newHorizontalValue < 1 || newHorizontalValue > 8
                        || newVerticalValue < 1 || newVerticalValue > 8) {
                    break;
                }
                PieceCoordinate newCoord = getCoordinate(newHorizontalValue, newVerticalValue);
                Piece existingPiece = board.getPiece(newCoord);
                if (existingPiece == null) {
                    possibleMoves.add(new ChessMove(this,newCoord));
                } else {
                    if (existingPiece.getColor() != this.getColor()) {
                        ChessMove to;
                        if(existingPiece instanceof King) {
                            to = new ChessMove(this,newCoord);
                            to.addTag(ChessMoveTags.AttacksKing);
                        }
                        else {
                            to = new ChessMove(this,newCoord,existingPiece);
                            to.addTag(ChessMoveTags.Capture);
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

    public Boolean canCastle(){
        return canCastle;
    }

}
