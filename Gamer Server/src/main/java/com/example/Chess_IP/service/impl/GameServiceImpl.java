package com.example.Chess_IP.service.impl;

import com.example.Chess_IP.domain.Game;
import com.example.Chess_IP.domain.Move;
import com.example.Chess_IP.repository.GameRepository;
import com.example.Chess_IP.repository.MoveRepository;
import com.example.Chess_IP.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    private final MoveValidator moveValidator = new MoveValidator();

    @Override
    public void processMove(String gameId, Move move) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));

        moveValidator.validateMove(move, game);

        move.setGame(game);
        moveRepository.save(move);

        game.setLastMove(move.getToPosition());
        game.setLastPlayerMove(move.getPlayer());
        gameRepository.save(game);
    }

    @Override
    public Game getGameState(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found: " + gameId));
    }
}

