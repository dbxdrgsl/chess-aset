package com.chess.demo.api.dto.auth;

public record MoveRequestDto(
        String gameId,
        String playerId,
        String from,
        String to
) {}