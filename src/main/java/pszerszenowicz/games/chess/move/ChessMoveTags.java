package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.ports.game.MoveTag;

public enum ChessMoveTags implements MoveTag {
    AttacksKing,Capture,Castle,EnPassant,Charge,PROMOTE_KNIGHT,PROMOTE_BISHOP,PROMOTE_ROOK,PROMOTE_QUEEN
}
