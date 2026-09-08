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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pszerszenowicz.games.chess.position.ChessBoard.getCoordinate;

public class Pawn extends Piece {

    public Pawn(PieceCoordinate pieceCoordinate, PieceColor color) {
        super(pieceCoordinate, color);
    }

    @Override
    public Set<ChessMove> getMoves(ChessPosition position) {
        ChessBoard board = position.getChessBoard();
        Set<ChessMove> possibleMoves = new HashSet<>();
        PieceColor color = getColor();
        int rowDir = color == PieceColor.WHITE ? 1 : -1;
        int columnValue = this.getPieceCoordinate().getColumn();
        int rowValue = this.getPieceCoordinate().getRow();
        final int promotionRow = color == PieceColor.WHITE ? 8 : 1;

        // Ruch do przodu
        possibleMoves.add(
                moveForward(columnValue, rowValue, rowDir, board)
        );

        // Bicie
        possibleMoves.addAll(
                capture(columnValue, rowValue, rowDir, board)
        );

        //Szarża
        possibleMoves.add(
                charge(columnValue, rowValue, rowDir, board)
        );

        //Bicie w locie
        PieceCoordinate enPassantSquare = position.getEnPassantSquare();
        if (enPassantSquare != null) {
            possibleMoves.add(
                    enPassant(columnValue, rowValue, rowDir, board, enPassantSquare)
            );
        }

        possibleMoves.remove(null);
        possibleMoves = possibleMoves.stream().flatMap(move ->
                move.to().getRow() == promotionRow
                        ? promotionMoves(move)
                        : Stream.of(move)
        ).collect(Collectors.toSet());
        return possibleMoves;
    }

    private Stream<ChessMove> promotionMoves(ChessMove move) {
        return Stream.of(
                new ChessMove(move, ChessMoveTags.PROMOTE_BISHOP),
                new ChessMove(move, ChessMoveTags.PROMOTE_KNIGHT),
                new ChessMove(move, ChessMoveTags.PROMOTE_ROOK),
                new ChessMove(move, ChessMoveTags.PROMOTE_QUEEN)
        );
    }

    private ChessMove moveForward(int columnValue, int rowValue, int rowDir, ChessBoard board) {
        PieceCoordinate newCoord = getCoordinate(columnValue, rowValue + rowDir);
        Piece existingPiece = board.getPiece(newCoord);
        if (existingPiece == null) {
            return new ChessMove(this, newCoord);
        }
        return null;
    }

    private Set<ChessMove> capture(int columnValue, int rowValue, int rowDir, ChessBoard board) {
        Set<ChessMove> ret = new HashSet<>();

        int[] horizontalDir = {-1, 1};
        for (int dir = 0; dir < 2; dir++) {
            int newHorVal = columnValue + horizontalDir[dir];
            int newVertVal = rowValue + rowDir;
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

    private ChessMove charge(int columnValue, int rowValue, int rowDir, ChessBoard board) {
        boolean charge = false;
        if (this.getColor() == PieceColor.WHITE) {
            if (rowValue == 2) {
                charge = true;
            }
        } else {
            if (rowValue == 7) {
                charge = true;
            }
        }
        if (charge) {
            PieceCoordinate newCoord = getCoordinate(columnValue, rowValue + rowDir);
            Piece existingPiece = board.getPiece(newCoord);
            if (existingPiece == null) {
                newCoord = getCoordinate(columnValue, rowValue + rowDir * 2);
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

    private ChessMove enPassant(int columnValue, int rowValue, int rowDir, ChessBoard board, PieceCoordinate enPassantSquare) {
        if (Math.abs(columnValue - enPassantSquare.getColumn()) - 1 == 0 &&
                rowValue + rowDir == enPassantSquare.getRow()) {
            Piece capturedPiece;
            if (this.getColor() == PieceColor.WHITE) {
                capturedPiece = board.getPiece(ChessBoard.getCoordinate(enPassantSquare.getColumn(),5));
            } else {
                capturedPiece = board.getPiece(ChessBoard.getCoordinate(enPassantSquare.getColumn(),4));
            }
            ChessMove move = new ChessMove(this, enPassantSquare, capturedPiece);
            move.addTag(ChessMoveTags.EnPassant);
            move.addTag(ChessMoveTags.Capture);
            return move;
        }
        return null;
    }
}
