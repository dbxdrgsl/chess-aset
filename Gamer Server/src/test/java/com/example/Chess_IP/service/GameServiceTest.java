package com.example.Chess_IP.service;

import com.example.Chess_IP.domain.Game;
import com.example.Chess_IP.domain.Move;
import com.example.Chess_IP.domain.Player;
import com.example.Chess_IP.repository.GameRepository;
import com.example.Chess_IP.repository.MoveRepository;
import com.example.Chess_IP.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MoveRepository moveRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private Player whitePlayer;
    private Player blackPlayer;
    private Game game;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        whitePlayer = new Player("player1", "Alice", false);
        blackPlayer = new Player("player2", "Bob", false);

        game = new Game("game1", whitePlayer, blackPlayer, null, null, "IN_PROGRESS");

        when(gameRepository.findById("game1")).thenReturn(Optional.of(game));
        when(gameRepository.findById("invalidId")).thenReturn(Optional.empty());
    }

    @Test
    void testGetGameState() {
        Game result = gameService.getGameState("game1");
        assertNotNull(result);
        assertEquals("IN_PROGRESS", result.getGameState());
    }

    @Test
    void testGetGameStateInvalidId() {
        assertThrows(RuntimeException.class, () -> gameService.getGameState("invalidId"));
    }

    @Test
    void testProcessMoveInvalidGame() {
        Move move = new Move("move1", null, whitePlayer, "e2", "e4", "Pawn", null, null);
        assertThrows(RuntimeException.class, () -> gameService.processMove("invalidGame", move));
    }

    @Test
    void testPlayerTurn() {
        game.setLastPlayerMove(whitePlayer);
        Move move = new Move("move2", game, whitePlayer, "e2", "e3", "Pawn", null, null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gameService.processMove("game1", move));
        assertEquals("It is not your turn", ex.getMessage());
    }

    @Test
    void testValidMovePersists() {
        Move move = new Move("move3", game, whitePlayer, "e2", "e3", "Pawn", null, null);
        gameService.processMove("game1", move);

        verify(moveRepository, times(1)).save(move);
        verify(gameRepository, times(1)).save(game);
        assertEquals("e3", game.getLastMove());
        assertEquals(whitePlayer, game.getLastPlayerMove());
    }
}
