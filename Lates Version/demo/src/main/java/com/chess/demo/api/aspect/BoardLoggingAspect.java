//package com.chess.demo.api.aspect;
//
//import com.chess.demo.api.domain.entity.Board;
//import com.chess.demo.api.domain.entity.Game;
//import com.chess.demo.api.domain.enums.ChessPiece;
//import com.chess.demo.api.misc.FenConvertor;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class BoardLoggingAspect {
//
//    @Before("execution(* com.chess.demo.api.service.MoveService.makeMove(..)) && args(game, from, to)")
//    public void logBeforeMove(JoinPoint joinPoint, Game game, String from, String to) {
//        System.out.println("==== MoveService.makeMove called ====");
//        System.out.println("Move: " + from + " -> " + to);
//
//        // Print FEN
//        String fen = FenConvertor.boardToFen(game.getBoard());
//        System.out.println("Board FEN before move: " + fen);
//
//        // Print visual board
//        printBoard(game.getBoard());
//
//        System.out.println("====================================");
//    }
//
//    @AfterReturning(pointcut = "execution(* com.chess.demo.api.service.MoveService.makeMove(..)) && args(game, from, to)", returning = "result")
//    public void logAfterMove(JoinPoint joinPoint, Game game, String from, String to, Object result) {
//        System.out.println("==== MoveService.makeMove completed ====");
//        System.out.println("Move executed: " + from + " -> " + to);
//
//        // Print FEN after move
//        String fen = FenConvertor.boardToFen(game.getBoard());
//        System.out.println("Board FEN after move: " + fen);
//
//        // Print visual board after move
//        printBoard(game.getBoard());
//
//        System.out.println("====================================");
//    }
//
//    private void printBoard(Board board) {
//        System.out.println("  a b c d e f g h");
//        for (int rank = 8; rank >= 1; rank--) {
//            System.out.print(rank + " ");
//            for (char file = 'a'; file <= 'h'; file++) {
//                ChessPiece piece = board.get("" + file + rank);
//                System.out.print(pieceToSymbol(piece) + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
//
//    private String pieceToSymbol(ChessPiece piece) {
//        if (piece == null) return ".";
//        return switch (piece.getType()) {
//            case "Pawn" -> piece.isWhite() ? "P" : "p";
//            case "Rook" -> piece.isWhite() ? "R" : "r";
//            case "Knight" -> piece.isWhite() ? "N" : "n";
//            case "Bishop" -> piece.isWhite() ? "B" : "b";
//            case "Queen" -> piece.isWhite() ? "Q" : "q";
//            case "King" -> piece.isWhite() ? "K" : "k";
//            default -> "?";
//        };
//    }
//}

package com.chess.demo.api.aspect;

import com.chess.demo.api.domain.entity.Board;
import com.chess.demo.api.domain.entity.Game;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.chess.demo.api.misc.FenConvertor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BoardLoggingAspect {

    @Before("execution(* com.chess.demo.api.service.MoveService.makeMove(..)) && args(game, from, to)")
    public void logBeforeMove(JoinPoint joinPoint, Game game, String from, String to) {
        System.out.println("==== MoveService.makeMove called ====");
        System.out.println("Move: " + from + " -> " + to);

        boolean whiteToMove = game.getCurrentTurnPlayer().equals(game.getWhitePlayer());
        String fen = FenConvertor.boardToFen(game.getBoard(), whiteToMove); // pass turn info
        System.out.println("Board FEN before move: " + fen);

        printBoard(game.getBoard());

        System.out.println("====================================");
    }

    @AfterReturning(pointcut = "execution(* com.chess.demo.api.service.MoveService.makeMove(..)) && args(game, from, to)", returning = "result")
    public void logAfterMove(JoinPoint joinPoint, Game game, String from, String to, Object result) {
        System.out.println("==== MoveService.makeMove completed ====");
        System.out.println("Move executed: " + from + " -> " + to);

        boolean whiteToMove = game.getCurrentTurnPlayer().equals(game.getWhitePlayer());
        String fen = FenConvertor.boardToFen(game.getBoard(), whiteToMove); // pass turn info
        System.out.println("Board FEN after move: " + fen);

        printBoard(game.getBoard());

        System.out.println("====================================");
    }

    private void printBoard(Board board) {
        System.out.println("  a b c d e f g h");
        for (int rank = 8; rank >= 1; rank--) {
            System.out.print(rank + " ");
            for (char file = 'a'; file <= 'h'; file++) {
                ChessPiece piece = board.get("" + file + rank);
                System.out.print(pieceToSymbol(piece) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String pieceToSymbol(ChessPiece piece) {
        if (piece == null) return ".";
        return switch (piece.getType()) {
            case "Pawn" -> piece.isWhite() ? "P" : "p";
            case "Rook" -> piece.isWhite() ? "R" : "r";
            case "Knight" -> piece.isWhite() ? "N" : "n";
            case "Bishop" -> piece.isWhite() ? "B" : "b";
            case "Queen" -> piece.isWhite() ? "Q" : "q";
            case "King" -> piece.isWhite() ? "K" : "k";
            default -> "?";
        };
    }
}
