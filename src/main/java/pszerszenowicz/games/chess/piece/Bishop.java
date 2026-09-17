package pszerszenowicz.games.chess.piece;

import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.games.chess.position.ChessBoard.getCoordinate;

public class Bishop extends Piece {
    public Bishop(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    @Override
    public Set<ChessMove> getMoves(ChessPosition position) {
        ChessBoard board = position.getChessBoard();
        Set<ChessMove> possibleMoves = new HashSet<>();
        int[] horizontalDir = {-1, -1, 1, 1};
        int[] verticalDir = {1, -1, -1, 1};
        for (int dir = 0; dir < 4; dir++) {
            int step = 1;
            while (true) {
                int newHorizontalValue = this.getPieceCoordinate().getColumn() + horizontalDir[dir] * step;
                int newVerticalValue = this.getPieceCoordinate().getRow() + verticalDir[dir] * step;

                if (newHorizontalValue < 1 || newHorizontalValue > 8
                        || newVerticalValue < 1 || newVerticalValue > 8) {
                    break;
                }

                PieceCoordinate newCoord = getCoordinate(newHorizontalValue,newVerticalValue);

                Piece existingPiece = board.getPiece(newCoord);
                if (existingPiece == null) {
                    possibleMoves.add(new ChessMove(this,newCoord));
                } else {
                    if (existingPiece.getColor() != this.getColor()) {
                        ChessMove to;
                        if(existingPiece instanceof King) {
                            to = new ChessMove(this,newCoord);
                            to.addTag(ChessMoveTags.ATTACKS_KING);
                        }
                        else{
                            to = new ChessMove(this,newCoord,existingPiece);
                            to.addTag(ChessMoveTags.CAPTURE);
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

    @Override
    public int getValue() {
        return 330;
    }
}
