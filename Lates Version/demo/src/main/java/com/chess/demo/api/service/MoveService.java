
package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.*;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.chess.demo.api.exception.CheckmateException;
import com.chess.demo.api.exception.MoveException;
import com.chess.demo.api.misc.ChesslibMapper;
import com.chess.demo.api.misc.FenConvertor;
import com.chess.demo.api.repository.GameRepository;
import com.chess.demo.api.repository.MoveRepository;
import com.github.bhlangonijr.chesslib.Square;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MoveService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;

    public boolean makeMove(Game game, String from, String to) {
        if (game.getStatus() != Game.GameStatus.IN_PROGRESS) {
            throw new MoveException(
                    "Game is not in progress. Current status: " + game.getStatus()
            );
        }
        try {
            boolean whiteToMove = game.getCurrentTurnPlayer().equals(game.getWhitePlayer());

            String fen = FenConvertor.boardToFen(game.getBoard(), whiteToMove);

            log.info("FEN before move: {}", fen);

            com.github.bhlangonijr.chesslib.Board libBoard = new com.github.bhlangonijr.chesslib.Board();
            libBoard.loadFromFen(fen);

            log.info("Legal moves available: {}", libBoard.legalMoves());

            if (libBoard.legalMoves().isEmpty()) {
                log.info("Legal moves available: {}", libBoard.legalMoves());
                if (libBoard.isKingAttacked()) {
                    log.info("Is King attacked: YES");
                    throw new MoveException("Checkmate! " + (whiteToMove ? "Black" : "White") + " wins.");
                } else {
                    log.info("Is King attacked: NO");
                    throw new MoveException("Stalemate! The game is a draw.");
                }
            }

            com.github.bhlangonijr.chesslib.move.Move move =
                    new com.github.bhlangonijr.chesslib.move.Move(
                            com.github.bhlangonijr.chesslib.Square.fromValue(from.toUpperCase()),
                            com.github.bhlangonijr.chesslib.Square.fromValue(to.toUpperCase())
                    );

            if (!libBoard.legalMoves().contains(move)) {
                throw new MoveException("Illegal move: " + from + " -> " + to);
            }
            libBoard.doMove(move);

            String newFen = libBoard.getFen();
            game.setBoard(FenConvertor.fenToBoard(newFen));

            log.info("FEN before move: {}", newFen);

            boolean opponentToMove = !whiteToMove;
            String opponentFen = FenConvertor.boardToFen(game.getBoard(), opponentToMove);

            com.github.bhlangonijr.chesslib.Board checkBoard = new com.github.bhlangonijr.chesslib.Board();
            checkBoard.loadFromFen(opponentFen);

//            if (checkBoard.legalMoves().isEmpty()) {
//                if (checkBoard.isKingAttacked()) {
//                    throw new CheckmateException("Checkmate! " + (opponentToMove ? "White" : "Black") + " wins.");
//                } else {
//                    throw new MoveException("Stalemate! The game is a draw.");
//                }
//            }

            return true;

        }catch (CheckmateException e) {
            throw new CheckmateException("CheckmateExceptionHere" + e.getMessage(), e);
        } catch (com.github.bhlangonijr.chesslib.move.MoveGeneratorException e) {
            throw new MoveException("No legal moves could be generated." + "\n" + e.getClass()+"\n" +  e.getMessage(), e);
        } catch (NullPointerException e) {
            throw new MoveException("Unexpected board state encountered." + e.getMessage(), e);
        } catch (Exception e) {
            throw new MoveException("Move failed due to unexpected error: " + "\n" + e.getClass()+"\n" +  e.getMessage(), e);
        }
    }

}
