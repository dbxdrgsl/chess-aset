package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.repository.GameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // for pretty print

    /**
     * This is the ONLY method used by LobbyService
     */
    public Game createGame(Player white, Player black) {
        Game game = new Game();
        game.setWhitePlayer(white);
        game.setBlackPlayer(black);
        game.setCurrentTurnPlayer(white);
        game.setStatus(Game.GameStatus.IN_PROGRESS);

        game.getBoard().initialize();

        return gameRepository.save(game);
    }
}