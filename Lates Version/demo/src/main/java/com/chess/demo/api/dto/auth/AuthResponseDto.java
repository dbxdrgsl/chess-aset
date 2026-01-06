package com.chess.demo.api.dto.auth;

import com.chess.demo.api.domain.enums.Role;

public record AuthResponseDto (String token, String email, Role role, Long userId) {
}
