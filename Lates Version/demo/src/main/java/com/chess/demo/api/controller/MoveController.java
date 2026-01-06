package com.chess.demo.api.controller;

import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.entity.MoveResult;
import com.chess.demo.api.dto.auth.MoveRequestDto;
import com.chess.demo.api.dto.game.MoveDto;
import com.chess.demo.api.exception.CheckmateException;
import com.chess.demo.api.exception.MoveException;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.service.MoveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/games/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "Games",
        description = "Endpoints for playing a chess game and retrieving game state"
)
@SecurityRequirement(name = "bearerAuth")
public class MoveController {

    private final MoveService moveService;
    private final GameRepository gameRepository;

    @Operation(
            summary = "Make a move",
            description = "Executes a chess move for the current player if the move is legal"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Move executed successfully"),
            @ApiResponse(responseCode = "400", description = "Illegal move or not player's turn"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
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

            try{
                boolean success = moveService.makeMove(game, request.from(), request.to());
                if (!success) {
                    return ResponseEntity.badRequest().body("Illegal move: " + request.from() + " -> " + request.to());
                }
            }catch (CheckmateException e) {
                if(game.getCurrentTurnPlayer().equals(game.getWhitePlayer()))
                    game.setStatus(Game.GameStatus.WHITE_WIN);
                else{
                    game.setStatus(Game.GameStatus.BLACK_WIN);
                }
                gameRepository.save(game);
                return ResponseEntity.ok("Move executed: " + request.from() + " -> " + request.to());
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

    @Operation(
            summary = "Get game state",
            description = "Returns the current state of the chess game by game ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Game found"),
            @ApiResponse(responseCode = "404", description = "Game not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<Map<String, Object>> getGame(@PathVariable String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("fen", game.getBoard());                    // the FEN string
        response.put("status", game.getStatus().name());         // IN_PROGRESS, WHITE_WIN, etc.
        response.put("whitePlayerId", game.getWhitePlayer().getPlayerId());
        response.put("blackPlayerId", game.getBlackPlayer().getPlayerId());
        response.put("currentTurnPlayerId", game.getCurrentTurnPlayer().getPlayerId());

        return ResponseEntity.ok(response);
    }
}