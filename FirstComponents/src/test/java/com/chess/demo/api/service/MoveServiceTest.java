//package com.chess.demo.api.service;
//
//
//import com.chess.demo.api.domain.entity.Board;
//import com.chess.demo.api.domain.entity.Game;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import com.chess.demo.api.exception.MoveException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MoveServiceTest {
//
//    private MoveService moveService;
//    private Game game;
//
//    @BeforeEach
//    void setUp() {
//        moveService = new MoveService(null, null); // repositories not needed for this test
//
//        // Create a game with initial board
//        game = new Game();
//        Board board = new Board();
//        board.initialize(); // standard chess starting position
//        game.setBoard(board);
//    }
//
//    @Test
//    void testLegalMove() {
//        // Move white pawn e2 -> e4
//        assertDoesNotThrow(() -> moveService.makeMove(game, "e2", "e4"));
//
//        // Check the board was updated
//        assertNull(game.getBoard().get("e2"), "e2 should be empty after move");
//        assertNotNull(game.getBoard().get("e4"), "e4 should have the pawn");
//        assertEquals("PAWN_WHITE_E", game.getBoard().get("e4").name());
//    }
//
//    @Test
//    void testIllegalMove() {
//        // Try to move black pawn from e7 on white's turn (illegal)
//        MoveException ex = assertThrows(MoveException.class,
//                () -> moveService.makeMove(game, "e7", "e5"));
//        assertEquals("Illegal move: e7 -> e5", ex.getMessage());
//    }
//
//    @Test
//    void testKnightMove() {
//        // Move white knight g1 -> f3
//        assertDoesNotThrow(() -> moveService.makeMove(game, "g1", "f3"));
//        assertNull(game.getBoard().get("g1"));
//        assertEquals("KNIGHT_WHITE_KINGSIDE", game.getBoard().get("f3").name());
//    }
//}
package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.Board;
import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.chess.demo.api.exception.MoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveServiceTest {

    private MoveService moveService;
    private Game game;

    @BeforeEach
    void setUp() {
        moveService = new MoveService(null, null); // repositories not needed for this test

        // Create a game with initial board
        game = new Game();
        Board board = new Board();
        board.initialize(); // standard chess starting position
        game.setBoard(board);
    }

    @Test
    void testLegalMove() {
        // Move white pawn e2 -> e4
        assertDoesNotThrow(() -> moveService.makeMove(game, "e2", "e4"));
        assertNull(game.getBoard().get("e2"));
        assertEquals("PAWN_WHITE_E", game.getBoard().get("e4").name());
    }

    @Test
    void testIllegalMove() {
        // Illegal: move black pawn from e7 on white's turn
        MoveException ex = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "e7", "e5"));
        assertEquals("Illegal move: e7 -> e5", ex.getMessage());
    }

    @Test
    void testKnightMove() {
        // Legal: white knight g1 -> f3
        assertDoesNotThrow(() -> moveService.makeMove(game, "g1", "f3"));
        assertNull(game.getBoard().get("g1"));
        assertEquals("KNIGHT_WHITE_KINGSIDE", game.getBoard().get("f3").name());
    }

    @Test
    void testMoreLegalMoves() {
        // Move pawn d2 -> d4
        assertDoesNotThrow(() -> moveService.makeMove(game, "d2", "d4"));
        assertNull(game.getBoard().get("d2"));
        assertEquals("PAWN_WHITE_D", game.getBoard().get("d4").name());

        // Move knight b1 -> c3
        assertDoesNotThrow(() -> moveService.makeMove(game, "b1", "c3"));
        assertNull(game.getBoard().get("b1"));
        assertEquals("KNIGHT_WHITE_QUEENSIDE", game.getBoard().get("c3").name());
    }

    @Test
    void testMoreIllegalMoves() {
        // Illegal: pawn moving backward
        MoveException ex1 = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "e2", "e1"));
        assertEquals("Illegal move: e2 -> e1", ex1.getMessage());

        // Illegal: rook jumping over pawn
        MoveException ex2 = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "a1", "a3"));
        assertEquals("Illegal move: a1 -> a3", ex2.getMessage());

        // Illegal: knight moving like a bishop
        MoveException ex3 = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "b1", "b3"));
        assertEquals("Illegal move: b1 -> b3", ex3.getMessage());

        // Illegal: king moving two squares forward
        MoveException ex4 = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "e1", "e3"));
        assertEquals("Illegal move: e1 -> e3", ex4.getMessage());
    }

    @Test
    void testCaptureMove() {
        // Place black pawn on d5 for capture
        game.getBoard().remove("d7");
        game.getBoard().place("d5", ChessPiece.PAWN_BLACK_D);
        game.getBoard().place("e4", ChessPiece.PAWN_WHITE_E);

        // White pawn captures black pawn: e4 -> d5
        assertDoesNotThrow(() -> moveService.makeMove(game, "e4", "d5"));

        assertNull(game.getBoard().get("e4")); // old square empty
        assertEquals("PAWN_WHITE_D", game.getBoard().get("d5").name()); // pawn is now on d5
    }


    @Test
    void testCastling() {
        // Clear path for queen-side castling
        game.getBoard().remove("b1");
        game.getBoard().remove("c1");
        game.getBoard().remove("d1");

        // King-side castling (e1 -> g1) requires empty f1 and g1 as well
        game.getBoard().remove("f1");
        game.getBoard().remove("g1");

        assertDoesNotThrow(() -> moveService.makeMove(game, "e1", "g1"));
        assertNull(game.getBoard().get("e1"));
        assertEquals("KING_WHITE", game.getBoard().get("g1").name());
    }
}
