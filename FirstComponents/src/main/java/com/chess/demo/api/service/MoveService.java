package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.*;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.chess.demo.api.exception.MoveException;
import com.chess.demo.api.misc.ChesslibMapper;
import com.chess.demo.api.misc.FenConvertor;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.repository.MoveRepository;
import com.github.bhlangonijr.chesslib.Square;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MoveService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;

    public boolean makeMove(Game game, String from, String to) {
        // whose turn it is
        boolean whiteToMove = game.getCurrentTurnPlayer().equals(game.getWhitePlayer());

        // Convert to fen
        String fen = FenConvertor.boardToFen(game.getBoard(), whiteToMove);

        com.github.bhlangonijr.chesslib.Board libBoard = new com.github.bhlangonijr.chesslib.Board();
        libBoard.loadFromFen(fen);

        if (libBoard.legalMoves().isEmpty()) {
            if (libBoard.isKingAttacked()) {
                throw new MoveException("Checkmate! " +
                        (whiteToMove ? "Black" : "White") + " wins.");
            } else {
                throw new MoveException("Stalemate! The game is a draw.");
            }
        }

        // convert to Move object
        com.github.bhlangonijr.chesslib.move.Move move =
                new com.github.bhlangonijr.chesslib.move.Move(
                        com.github.bhlangonijr.chesslib.Square.fromValue(from.toUpperCase()),
                        com.github.bhlangonijr.chesslib.Square.fromValue(to.toUpperCase())
                );

        // validation
        if (!libBoard.legalMoves().contains(move)) {
            throw new MoveException("Illegal move: " + from + " -> " + to);
        }
        libBoard.doMove(move);

        String newFen = libBoard.getFen();
        game.setBoard(FenConvertor.fenToBoard(newFen));

        boolean opponentToMove = !whiteToMove; // switch turn
        String opponentFen = FenConvertor.boardToFen(game.getBoard(), opponentToMove);

        com.github.bhlangonijr.chesslib.Board checkBoard = new com.github.bhlangonijr.chesslib.Board();
        checkBoard.loadFromFen(opponentFen);

        if (checkBoard.legalMoves().isEmpty()) {
            if (checkBoard.isKingAttacked()) {
                throw new MoveException(
                        "Checkmate! " + (opponentToMove ? "White" : "Black") + " wins."
                );
            } else {
                throw new MoveException("Stalemate! The game is a draw.");
            }
        }

        return true;
    }

}
