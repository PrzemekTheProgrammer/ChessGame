package pszerszenowicz.games.chess.move;

import pszerszenowicz.domain.ports.Tag;

public enum ChessMoveTags implements Tag {
    AttacksKing,Capture,Castle,EnPassant,Charge,PROMOTE_KNIGHT,PROMOTE_BISHOP,PROMOTE_ROOK,PROMOTE_QUEEN
}
