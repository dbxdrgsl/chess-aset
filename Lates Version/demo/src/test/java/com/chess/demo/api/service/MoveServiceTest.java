package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.Board;
import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.chess.demo.api.exception.CheckmateException;
import com.chess.demo.api.exception.MoveException;
import com.chess.demo.api.misc.FenConvertor;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.repository.MoveRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MoveServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MoveRepository moveRepository;

    @InjectMocks
    private MoveService moveService;

    private Game game;
    private Player white;
    private Player black;

    @BeforeEach
    void setUp() {
        white = new Player();
        white.setPlayerId(1L);

        black = new Player();
        black.setPlayerId(2L);

        game = new Game();
        game.setWhitePlayer(white);
        game.setBlackPlayer(black);
        game.setCurrentTurnPlayer(white);

        game.getBoard().initialize();
    }

    // ----------------------------
    // LEGAL MOVE (pawn)
    // ----------------------------
    @Test
    void makeMove_legalMove_returnsTrue() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        boolean result = moveService.makeMove(game, "e2", "e4");
        assertTrue(result);
        assertNull(game.getBoard().get("e2"));
        assertEquals("PAWN_WHITE_E", game.getBoard().get("e4").name());
    }

    // ----------------------------
    // ILLEGAL MOVE
    // ----------------------------
    @Test
    void makeMove_illegalMove_throwsException() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        MoveException ex = assertThrows(
                MoveException.class,
                () -> moveService.makeMove(game, "e2", "e5")
        );
        assertTrue(ex.getMessage().contains("Illegal move"));
    }

    // ----------------------------
    // KNIGHT MOVE
    // ----------------------------
    @Test
    void makeMove_knightMove_succeeds() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        boolean result = moveService.makeMove(game, "g1", "f3");
        assertTrue(result);
        assertNull(game.getBoard().get("g1"));
        assertEquals("KNIGHT_WHITE_KINGSIDE", game.getBoard().get("f3").name());
    }

    // ----------------------------
    // PAWN CAPTURE
    // ----------------------------
    @Test
    void makeMove_pawnCapture_succeeds() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        game.getBoard().remove("d7");
        game.getBoard().place("d5", ChessPiece.PAWN_BLACK_D);
        game.getBoard().place("e4", ChessPiece.PAWN_WHITE_E);

        boolean result = moveService.makeMove(game, "e4", "d5");
        assertTrue(result);
        assertNull(game.getBoard().get("e4"));
        assertEquals("PAWN_WHITE_D", game.getBoard().get("d5").name());
    }

    // ----------------------------
    // TURN SWITCHING
    // ----------------------------
    @Test
    void makeMove_turnSwitching_succeeds() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        // White moves e2 -> e4
        boolean whiteMove = moveService.makeMove(game, "e2", "e4");
        assertTrue(whiteMove);

        // Place a black pawn at e7
        game.getBoard().place("e7", ChessPiece.PAWN_BLACK_E);
        game.setCurrentTurnPlayer(black); // switch manually

        // Black moves e7 -> e5
        boolean blackMove = moveService.makeMove(game, "e7", "e5");
        assertTrue(blackMove);
    }

    // ----------------------------
    // CASTLING (KING SIDE)
    // ----------------------------
    @Test
    void makeMove_castlingKingSide_succeeds() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        game.getBoard().remove("f1");
        game.getBoard().remove("g1");

        boolean result = moveService.makeMove(game, "e1", "g1");
        assertTrue(result);
        assertNull(game.getBoard().get("e1"));
        assertEquals("KING_WHITE", game.getBoard().get("g1").name());
    }

    // ----------------------------
    // CASTLING (QUEEN SIDE)
    // ----------------------------
    @Test
    void makeMove_castlingQueenSide_succeeds() {
        game.getBoard().remove("b1");
        game.getBoard().remove("c1");
        game.getBoard().remove("d1");
        game.setStatus(Game.GameStatus.IN_PROGRESS);

        boolean result = moveService.makeMove(game, "e1", "c1");
        assertTrue(result);
        assertNull(game.getBoard().get("e1"));
        assertEquals("KING_WHITE", game.getBoard().get("c1").name());
    }

    // ----------------------------
    // MOVE WRONG COLOR
    // ----------------------------
    @Test
    void makeMove_wrongColor_throwsException() {
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        game.setCurrentTurnPlayer(black);
        MoveException ex = assertThrows(
                MoveException.class,
                () -> moveService.makeMove(game, "e2", "e4")
        );
        assertTrue(ex.getMessage().contains("Illegal move"));
    }


    @Test
    void makeMove_nullPointerException_throwsMoveException() {
        Game game = new Game();
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        game.setWhitePlayer(white);
        game.setBlackPlayer(black);
        game.setCurrentTurnPlayer(white);

        // Manipulate the board to trigger NPE
        game.setBoard(null);

        MoveException ex = assertThrows(MoveException.class,
                () -> moveService.makeMove(game, "e2", "e4"));

        assertTrue(ex.getMessage().contains("Unexpected board state encountered"));
    }

    @Test
    void makeMove_checkmate_throwsMoveException() {
        // White is checkmated
        game.setStatus(Game.GameStatus.IN_PROGRESS);
        String checkmateFen =
                "rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 0 3";

        game.setBoard(FenConvertor.fenToBoard(checkmateFen));
        game.setCurrentTurnPlayer(white);

        MoveException ex = assertThrows(
                MoveException.class,
                () -> moveService.makeMove(game, "a2", "a3") // any move, none exist
        );

        assertTrue(ex.getMessage().contains("Checkmate"));
        assertTrue(ex.getMessage().contains("Black wins"));
    }

    @Test
    void makeMove_stalemate_throwsMoveException() {
        String stalemateFen =
                "7k/5Q2/6K1/8/8/8/8/8 b - - 0 1";

        game.setStatus(Game.GameStatus.IN_PROGRESS);
        game.setBoard(FenConvertor.fenToBoard(stalemateFen));
        game.setCurrentTurnPlayer(black);

        MoveException ex = assertThrows(
                MoveException.class,
                () -> moveService.makeMove(game, "h8", "h7")
        );

        // 🔍 DEBUG LOGS
        System.out.println("==== TEST DEBUG ====");
        System.out.println("Exception class: " + ex.getClass().getSimpleName());
        System.out.println("Exception message: " + ex.getMessage());
        System.out.println("====================");

        assertTrue(ex.getMessage().contains("No legal moves could be generated"));
    }

    // ----------------------------
    // GAME IN PROGRESS
    // ----------------------------
    @Test
    void makeMove_gameInProgress_succeeds() {
        // Ensure game status is IN_PROGRESS
        game.setStatus(Game.GameStatus.IN_PROGRESS);

        // Make a legal move
        boolean result = moveService.makeMove(game, "e2", "e4");

        assertTrue(result);
        assertNull(game.getBoard().get("e2"));
        assertEquals("PAWN_WHITE_E", game.getBoard().get("e4").name());
    }

    // ----------------------------
// GAME NOT IN PROGRESS
// ----------------------------
    @Test
    void makeMove_gameNotInProgress_throwsException() {
        // Set game status to something other than IN_PROGRESS
        game.setStatus(Game.GameStatus.WAITING);

        // Attempt to make a move
        MoveException ex = assertThrows(
                MoveException.class,
                () -> moveService.makeMove(game, "e2", "e4")
        );

        // Verify the exception message
        assertTrue(ex.getMessage().contains("Game is not in progress"));
        assertTrue(ex.getMessage().contains("WAITING"));
    }

}
