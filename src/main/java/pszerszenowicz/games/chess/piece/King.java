package pszerszenowicz.games.chess.piece;

import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceCoordinate;
import pszerszenowicz.games.chess.position.CastlingRights;
import pszerszenowicz.games.chess.position.ChessBoard;
import pszerszenowicz.games.chess.position.ChessPosition;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.games.chess.position.ChessBoard.*;

public class King extends Piece {
    public King(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    @Override
    public Set<ChessMove> getMoves(ChessPosition position) {
        ChessBoard board = position.getChessBoard();
        Set<ChessMove> possibleMoves = new HashSet<>();
        PieceCoordinate from = this.getPieceCoordinate();
        int[] columnDir = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] verticalDir = {1, 0, -1, -1, -1, 0, 1, 1};
        for (int dir = 0; dir < 8; dir++) {
            int newColumnValue = from.getColumn() + columnDir[dir];
            int newRowValue = from.getRow() + verticalDir[dir];
            if (newColumnValue < 1 || newColumnValue > 8
                    || newRowValue < 1 || newRowValue > 8) {
                continue;
            }
            PieceCoordinate newCoord = getCoordinate(
                    newColumnValue,
                    newRowValue);

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
                        to = new ChessMove(this, newCoord, existingPiece);
                        to.addTag(ChessMoveTags.Capture);
                    }
                    possibleMoves.add(to); // bicie
                }
            }
        }

        columnDir = new int[]{-1, 1};
        for (int dir = 0; dir < 2; dir++) {
            if (!canCastle(position.getCastlingRights(), columnDir[dir]))
                continue;
            int step = 1;
            int newColumnValue = from.getColumn() + columnDir[dir] * step;
            do {
                PieceCoordinate newCoord = getCoordinate(
                        newColumnValue,
                        from.getRow());
                Piece existingPiece = board.getPiece(newCoord);
                if (newColumnValue != 1 && newColumnValue != 8) {
                    if (existingPiece == null) {
                        step++;
                        newColumnValue = from.getColumn() + columnDir[dir] * step;
                        continue;
                    } else {
                        break;
                    }
                }
                if (existingPiece instanceof Rook rook){
                    if ( existingPiece.getPieceCoordinate().getColumn() == 1) {
                        if (getColor() == PieceColor.WHITE) {
                            if (position.getCastlingRights().hasRight(CastlingRights.WHITE_QUEEN_SIDE)) {
                                possibleMoves.add(castleMove(C1,rook));
                            }
                        }
                        else {
                            if(position.getCastlingRights().hasRight(CastlingRights.BLACK_QUEEN_SIDE)) {
                                possibleMoves.add(castleMove(C8,rook));
                            }
                        }
                    }
                    else if (existingPiece.getPieceCoordinate().getColumn() == 8) {
                        if (getColor() == PieceColor.WHITE) {
                            if (position.getCastlingRights().hasRight(CastlingRights.WHITE_KING_SIDE)) {
                                possibleMoves.add(castleMove(G1,rook));
                            }
                        }
                        else {
                            if(position.getCastlingRights().hasRight(CastlingRights.BLACK_KING_SIDE)) {
                                possibleMoves.add(castleMove(G8,rook));
                            }
                        }
                    }
                }
                break;
            } while (newColumnValue <= 8 && newColumnValue >= 1);

        }
        return possibleMoves;
    }

    @Override
    public int getValue() {
        return 20000;
    }

    ChessMove castleMove(PieceCoordinate coordinate, Rook rook) {
        ChessMove ret = new ChessMove(this,coordinate,rook);
        ret.addTag(ChessMoveTags.Castle);
        return ret;
    }

    private boolean canCastle(CastlingRights castlingRights, int dir) {
        if (this.getColor() == PieceColor.WHITE) {
            if (dir < 0) {
                return castlingRights.hasRight(CastlingRights.WHITE_QUEEN_SIDE);
            } else {
                return castlingRights.hasRight(CastlingRights.WHITE_KING_SIDE);
            }
        } else {
            if (dir < 0) {
                return castlingRights.hasRight(CastlingRights.BLACK_QUEEN_SIDE);
            } else {
                return castlingRights.hasRight(CastlingRights.BLACK_KING_SIDE);
            }
        }
    }
}
