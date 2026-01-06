package com.chess.demo.api.controller;

import com.chess.demo.api.domain.entity.Lobby;
import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.domain.entity.UserEntity;
import com.chess.demo.api.service.LobbyService;
import com.chess.demo.api.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin(origins = "*")
@Tag(
        name = "Lobby",
        description = "Endpoints for joining and checking the status of the game lobby"
)
@SecurityRequirement(name = "bearerAuth")
public class GameLobbyController {

    private final LobbyService lobbyService;
    private final PlayerService playerService;

    @Operation(
            summary = "Join the game lobby",
            description = "Adds the authenticated player to an available lobby or creates a new one"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully joined a lobby"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PLAYER role")
    })
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

    @Operation(
            summary = "Get current lobby status",
            description = "Returns the active lobby of the authenticated player, if any"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lobby found"),
            @ApiResponse(responseCode = "204", description = "Player is not currently in a lobby"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden – user does not have PLAYER role")
    })
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