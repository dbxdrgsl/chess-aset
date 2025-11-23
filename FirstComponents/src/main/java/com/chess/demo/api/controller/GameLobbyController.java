package com.chess.demo.api.controller;

import com.chess.demo.api.domain.entity.Lobby;
import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.domain.entity.UserEntity;
import com.chess.demo.api.service.LobbyService;
import com.chess.demo.api.service.PlayerService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lobby")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
public class GameLobbyController {

    private final LobbyService lobbyService;
    private final PlayerService playerService;

    @PostMapping("/join")
    public ResponseEntity<Lobby> joinLobby(Authentication authentication) {
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();

        Player player = playerService.findByEmail(email);
        if (player == null) {
            throw new RuntimeException("Player not found for email: " + email);
        }

        Lobby lobby = lobbyService.joinLobby(player.getPlayerId());
        return ResponseEntity.ok(lobby);
    }

    @GetMapping("/status")
    public ResponseEntity<Lobby> getMyLobby(Authentication authentication) {
        User springUser = (User) authentication.getPrincipal();
        String email = springUser.getUsername();

        Player player = playerService.findByEmail(email);
        if (player == null) {
            return ResponseEntity.noContent().build();
        }

        Lobby lobby = lobbyService.getActiveLobbyForPlayer(player.getPlayerId());
        return lobby != null ? ResponseEntity.ok(lobby) : ResponseEntity.noContent().build();
    }
}