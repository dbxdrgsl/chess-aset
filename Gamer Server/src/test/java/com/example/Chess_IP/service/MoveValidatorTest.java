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

public class MoveValidatorTest {

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

        // Mock repository behavior
        when(gameRepository.findById("game1")).thenReturn(Optional.of(game));
        when(gameRepository.findById("invalidId")).thenReturn(Optional.empty());
    }

    @Test
    void testMoveOutsideBoard() {
        Move move = new Move("m1", game, whitePlayer, "i2", "i4", "Pawn", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Move is outside of the board", ex.getMessage());
    }

    @Test
    void testPawnInvalidMove() {
        Move move = new Move("m2", game, whitePlayer, "e2", "e5", "Pawn", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid pawn move distance", ex.getMessage());
    }

    @Test
    void testValidPawnMove() {
        Move move = new Move("m3", game, whitePlayer, "e2", "e3", "Pawn", null, null);
        gameService.processMove("game1", move);
        assertEquals("e3", game.getLastMove());
    }

    @Test
    void testValidRookMove() {
        Move move = new Move("m4", game, whitePlayer, "a1", "a4", "Rook", null, null);
        gameService.processMove("game1", move);
        assertEquals("a4", game.getLastMove());
    }

    @Test
    void testInvalidRookMove() {
        Move move = new Move("m5", game, whitePlayer, "a1", "b2", "Rook", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid rook move: must move straight", ex.getMessage());
    }

    @Test
    void testValidBishopMove() {
        Move move = new Move("m6", game, whitePlayer, "c1", "f4", "Bishop", null, null);
        gameService.processMove("game1", move);
        assertEquals("f4", game.getLastMove());
    }

    @Test
    void testInvalidBishopMove() {
        Move move = new Move("m7", game, whitePlayer, "c1", "c3", "Bishop", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid bishop move: must move diagonally", ex.getMessage());
    }

    @Test
    void testValidKnightMove() {
        Move move = new Move("m8", game, whitePlayer, "b1", "c3", "Knight", null, null);
        gameService.processMove("game1", move);
        assertEquals("c3", game.getLastMove());
    }

    @Test
    void testInvalidKnightMove() {
        Move move = new Move("m9", game, whitePlayer, "b1", "b3", "Knight", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid knight move: must move in L-shape", ex.getMessage());
    }

    @Test
    void testValidQueenMove() {
        Move move = new Move("m10", game, whitePlayer, "d1", "h5", "Queen", null, null);
        gameService.processMove("game1", move);
        assertEquals("h5", game.getLastMove());
    }

    @Test
    void testInvalidQueenMove() {
        Move move = new Move("m11", game, whitePlayer, "d1", "e3", "Queen", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid queen move: must move straight or diagonal", ex.getMessage());
    }

    @Test
    void testValidKingMove() {
        Move move = new Move("m12", game, whitePlayer, "e1", "e2", "King", null, null);
        gameService.processMove("game1", move);
        assertEquals("e2", game.getLastMove());
    }

    @Test
    void testInvalidKingMove() {
        Move move = new Move("m13", game, whitePlayer, "e1", "e3", "King", null, null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> gameService.processMove("game1", move));
        assertEquals("Invalid king move: must move one square", ex.getMessage());
    }
}
