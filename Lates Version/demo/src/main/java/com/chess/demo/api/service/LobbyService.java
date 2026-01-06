package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.entity.Lobby;
import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.repository.LobbyRepository;
import com.chess.demo.api.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final GameService gameService; // your existing service

    public Lobby joinLobby(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        Lobby existingLobby = getActiveLobbyForPlayer(playerId);
        if (existingLobby != null) {
            return existingLobby;
        }

        // try to find latest WAITING lobby
        Lobby lobby = lobbyRepository.findLatestWaitingLobby()
                .orElseGet(this::createNewLobby);

        if (lobby.getPlayer1Id() == null) {
            lobby.setPlayer1Id(playerId);
        } else if (lobby.getPlayer2Id() == null && !lobby.getPlayer1Id().equals(playerId)) {
            lobby.setPlayer2Id(playerId);
            lobby.setStatus(Lobby.Status.STARTED);

            // auto-create the actual chess game
            Player white = playerRepository.getReferenceById(lobby.getPlayer1Id());
            Player black = playerRepository.getReferenceById(playerId);
            Game game = gameService.createGame(white, black); // uses your existing method
            lobby.setGameId(game.getGameId());
        } else {
            // should not happen (same player or full), but create new lobby just in case
            lobby = createNewLobby();
            lobby.setPlayer1Id(playerId);
        }

        return lobbyRepository.save(lobby);
    }

    private Lobby createNewLobby() {
        return Lobby.builder()
                .status(Lobby.Status.WAITING)
                .build();
    }

    public Lobby getActiveLobbyForPlayer(Long playerId) {
        return lobbyRepository.findAll().stream()
                .filter(l -> (l.getPlayer1Id() != null && l.getPlayer1Id().equals(playerId)) ||
                        (l.getPlayer2Id() != null && l.getPlayer2Id().equals(playerId)))
                .findFirst()
                .orElse(null);
    }
}