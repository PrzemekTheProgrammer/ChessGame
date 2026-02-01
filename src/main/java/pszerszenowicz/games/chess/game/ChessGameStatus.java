package pszerszenowicz.games.chess.game;

import pszerszenowicz.domain.ports.game.GameStatus;

public enum ChessGameStatus implements GameStatus {
    WHITE_WIN,BLACK_WIN,STALEMATE,ONGOING
}
