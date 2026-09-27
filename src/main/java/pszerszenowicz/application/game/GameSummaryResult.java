package pszerszenowicz.application.game;

import pszerszenowicz.games.chess.game.GameStatus;

import java.util.UUID;

public record GameSummaryResult(
        UUID id,
        String opponent,
        String color,
        GameStatus status
) {
}