package pszerszenowicz.chess.adapters.piece;

import pszerszenowicz.domain.adapters.Player;
import pszerszenowicz.ports.move.MoveTags;
import pszerszenowicz.ports.board.Board;
import pszerszenowicz.ports.move.Move;
import pszerszenowicz.ports.piece.Piece;
import pszerszenowicz.ports.piece.PieceColor;
import pszerszenowicz.ports.piece.PieceCoordinate;

import java.util.HashSet;
import java.util.Set;

import static pszerszenowicz.chess.adapters.board.ChessBoard.*;

public class Pawn extends Piece {

    public Pawn(PieceCoordinate pieceCoordinate, Player player) {
        super(pieceCoordinate, player);
    }

    @Override
    public Set<Move> getMoves(Board board) {
        Set<Move> possibleMoves = new HashSet<>();

        PieceCoordinate from = this.getPieceCoordinate();
        PieceColor color = getPlayer().getPieceColor();
        int verticalDir = color == PieceColor.WHITE ? 1 : -1;
        int horizontalValue = this.getPieceCoordinate().getColumn();
        int verticalValue = this.getPieceCoordinate().getRow();

        // Ruch do przodu
        possibleMoves.add(
                moveForward(from, horizontalValue, verticalValue, verticalDir, board)
        );

        // Bicie
        possibleMoves.addAll(
                capture(from, horizontalValue, verticalValue, verticalDir, board)
        );

        //Szarża
        possibleMoves.add(
                charge(from, horizontalValue, verticalValue, verticalDir, board)
        );

        //Bicie w locie
        possibleMoves.addAll(
                enPassant(from, horizontalValue, verticalValue, verticalDir, board, color)
        );

        possibleMoves.remove(null);

        return possibleMoves;
    }

    private Move moveForward(PieceCoordinate from, int horVal, int vertVal, int vertDir, Board board) {
        PieceCoordinate newCoord = getCoordinate(horVal, vertVal  + vertDir);
        Piece existingPiece = board.getPieceCoordinate().get(newCoord);
        if (existingPiece == null) {
            return new Move(this, newCoord);
        }
        return null;
    }

    private Set<Move> capture(PieceCoordinate from, int horVal, int vertVal, int vertDir, Board board) {
        Set<Move> ret = new HashSet<>();

        int horizontalDir[] = {-1, 1};
        for (int dir = 0; dir < 2; dir++) {
            int newHorVal = horVal + horizontalDir[dir];
            int newVertVal = vertVal + vertDir;
            if (newHorVal < 1 || newHorVal > 8)
                continue;
            PieceCoordinate newCoord = getCoordinate(newHorVal, newVertVal);
            Piece existingPiece = board.getPieceCoordinate().get(newCoord);
            if (existingPiece != null && existingPiece.getPlayer() != this.getPlayer()) {
                Move move = new Move(this, newCoord);
                if (existingPiece instanceof King) {
                    move.addTag(MoveTags.AttacksKing);
                } else {
                    move.addTag(MoveTags.Capture);
                }
                ret.add(move);
            }
        }
        return ret;
    }

    private Move charge(PieceCoordinate from, int horVal, int vertVal, int vertDir, Board board) {
        Boolean charge = false;
        if (this.getPlayer().getPieceColor() == PieceColor.WHITE) {
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
            Piece existingPiece = board.getPieceCoordinate().get(newCoord);
            if (existingPiece == null) {
                newCoord = getCoordinate(horVal, vertVal + vertDir * 2);
                existingPiece = board.getPieceCoordinate().get(newCoord);
                if (existingPiece == null) {
                    Move move = new Move(this, newCoord);
                    move.addTag(MoveTags.Charge);
                    return move;
                }
            }
        }
        return null;
    }

    private Set<Move> enPassant(PieceCoordinate from, int horVal, int vertVal, int vertDir, Board board, PieceColor color) {
        Boolean enPassant = false;
        int horizontalDir[] = {-1, 1};
        Set<Move> ret = new HashSet<>();

        if (color == PieceColor.WHITE
                && vertVal == 5) {
            enPassant = true;
        } else if (color == PieceColor.BLACK
                && vertVal == 4) {
            enPassant = true;
        }
        if (enPassant) {
            for (int hd = 0; hd < horizontalDir.length; hd++) {
                int newHorVal = horVal + horizontalDir[hd];
                if (newHorVal < 1 || newHorVal > 8)
                    break;
                PieceCoordinate newCoord = getCoordinate(newHorVal, vertVal);
                Piece existingPiece = board.getPieceCoordinate().get(newCoord);
                if (existingPiece != null
                        && existingPiece.getPlayer().getPieceColor() != this.getPlayer().getPieceColor()
                        && existingPiece instanceof Pawn) {
                    newCoord = getCoordinate(horVal + horizontalDir[hd], vertVal + vertDir
                    );
                    Move move = new Move(this, newCoord);
                    move.addTag(MoveTags.EnPassant);
                    move.addTag(MoveTags.Capture);
                    ret.add(move);
                }
            }
        }
        return ret;
    }
}
