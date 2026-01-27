package pszerszenowicz.games.chess.piece;

import pszerszenowicz.domain.ports.Board;
import pszerszenowicz.games.chess.board.ChessBoard;
import pszerszenowicz.games.chess.move.ChessMoveTags;
import pszerszenowicz.games.chess.move.ChessMove;
import pszerszenowicz.domain.core.piece.Piece;
import pszerszenowicz.domain.core.piece.PieceColor;
import pszerszenowicz.domain.core.piece.PieceCoordinate;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.games.chess.board.ChessBoard.getCoordinate;

public class Pawn extends Piece {

    public Pawn(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    @Override
    public Set<ChessMove> getMoves(Board board) {
        Set<ChessMove> possibleMoves = new HashSet<>();
        if (board instanceof ChessBoard) {
            PieceColor color = getColor();
            int verticalDir = color == PieceColor.WHITE ? 1 : -1;
            int horizontalValue = this.getPieceCoordinate().getColumn();
            int verticalValue = this.getPieceCoordinate().getRow();

            // Ruch do przodu
            possibleMoves.add(
                    moveForward(horizontalValue, verticalValue, verticalDir, (ChessBoard) board)
            );

            // Bicie
            possibleMoves.addAll(
                    capture(horizontalValue, verticalValue, verticalDir, (ChessBoard) board)
            );

            //Szarża
            possibleMoves.add(
                    charge(horizontalValue, verticalValue, verticalDir, (ChessBoard) board)
            );

            //Bicie w locie
            possibleMoves.addAll(
                    enPassant(horizontalValue, verticalValue, verticalDir, (ChessBoard) board, color)
            );

            possibleMoves.remove(null);
        }
        return possibleMoves;
    }

    private ChessMove moveForward(int horVal, int vertVal, int vertDir, ChessBoard board) {
        PieceCoordinate newCoord = getCoordinate(horVal, vertVal + vertDir);
        Piece existingPiece = board.getPiece(newCoord);
        if (existingPiece == null) {
            return new ChessMove(this, newCoord);
        }
        return null;
    }

    private Set<ChessMove> capture(int horVal, int vertVal, int vertDir, ChessBoard board) {
        Set<ChessMove> ret = new HashSet<>();

        int[] horizontalDir = {-1, 1};
        for (int dir = 0; dir < 2; dir++) {
            int newHorVal = horVal + horizontalDir[dir];
            int newVertVal = vertVal + vertDir;
            if (newHorVal < 1 || newHorVal > 8)
                continue;
            PieceCoordinate newCoord = getCoordinate(newHorVal, newVertVal);
            Piece existingPiece = board.getPiece(newCoord);
            if (existingPiece != null && existingPiece.getColor() != this.getColor()) {
                ChessMove move;
                if (existingPiece instanceof King) {
                    move = new ChessMove(this, newCoord);
                    move.addTag(ChessMoveTags.AttacksKing);
                } else {
                    move = new ChessMove(this, newCoord, existingPiece);
                    move.addTag(ChessMoveTags.Capture);
                }
                ret.add(move);
            }
        }
        return ret;
    }

    private ChessMove charge(int horVal, int vertVal, int vertDir, ChessBoard board) {
        boolean charge = false;
        if (this.getColor() == PieceColor.WHITE) {
            if (vertVal == 2) {
                charge = true;
            }
        } else {
            if (vertVal == 7) {
                charge = true;
            }
        }
        if (charge) {
            PieceCoordinate newCoord = getCoordinate(horVal, vertVal + vertDir);
            Piece existingPiece = board.getPiece(newCoord);
            if (existingPiece == null) {
                newCoord = getCoordinate(horVal, vertVal + vertDir * 2);
                existingPiece = board.getPiece(newCoord);
                if (existingPiece == null) {
                    ChessMove move = new ChessMove(this, newCoord);
                    move.addTag(ChessMoveTags.Charge);
                    return move;
                }
            }
        }
        return null;
    }

    private Set<ChessMove> enPassant(int horVal, int vertVal, int vertDir, ChessBoard board, PieceColor color) {
        boolean enPassant = false;
        int[] horizontalDir = {-1, 1};
        Set<ChessMove> ret = new HashSet<>();

        if (color == PieceColor.WHITE
                && vertVal == 5) {
            enPassant = true;
        } else if (color == PieceColor.BLACK
                && vertVal == 4) {
            enPassant = true;
        }
        if (enPassant) {
            for (int i : horizontalDir) {
                int newHorVal = horVal + i;
                if (newHorVal < 1 || newHorVal > 8)
                    break;
                PieceCoordinate newCoord = getCoordinate(newHorVal, vertVal);
                Piece existingPiece = board.getPiece(newCoord);
                if (existingPiece != null
                        && existingPiece.getColor() != this.getColor()
                        && existingPiece instanceof Pawn) {
                    newCoord = getCoordinate(horVal + i, vertVal + vertDir
                    );
                    ChessMove move = new ChessMove(this, newCoord, existingPiece);
                    move.addTag(ChessMoveTags.EnPassant);
                    move.addTag(ChessMoveTags.Capture);
                    ret.add(move);
                }
            }
        }
        return ret;
    }
}
