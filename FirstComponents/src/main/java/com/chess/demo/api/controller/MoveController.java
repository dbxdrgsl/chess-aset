package com.chess.demo.api.controller;

import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.entity.MoveResult;
import com.chess.demo.api.dto.auth.MoveRequestDto;
import com.chess.demo.api.dto.game.MoveDto;
import com.chess.demo.api.exception.MoveException;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.service.MoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games/")
@RequiredArgsConstructor
public class MoveController {

    private final MoveService moveService;
    private final GameRepository gameRepository;

    @PostMapping("/moves")
    public ResponseEntity<?> makeMove(@RequestBody MoveRequestDto request) {
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        try {
            Long playerId = Long.parseLong(request.playerId());

            // Check if it's the player's turn
            if (!game.getCurrentTurnPlayer().getPlayerId().equals(playerId)) {
                return ResponseEntity.badRequest().body("It's not your turn!");
            }

            boolean success = moveService.makeMove(game, request.from(), request.to());
            if (!success) {
                return ResponseEntity.badRequest().body("Illegal move: " + request.from() + " -> " + request.to());
            }

            // Switch turn to the other player
            if (game.getCurrentTurnPlayer().equals(game.getWhitePlayer())) {
                game.setCurrentTurnPlayer(game.getBlackPlayer());
            } else {
                game.setCurrentTurnPlayer(game.getWhitePlayer());
            }

            gameRepository.save(game);
            return ResponseEntity.ok("Move executed: " + request.from() + " -> " + request.to());
        } catch (MoveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}