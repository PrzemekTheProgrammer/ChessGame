package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.ports.Tag;

public enum ChessGameStatus implements Tag {
    WHITE_WIN,BLACK_WIN,STALEMATE,ONGOING
}
