package com.chess.demo.api.misc;

import com.chess.demo.api.domain.entity.Board;

public class FenConvertorTest {

    public static void main(String[] args) {
        testInitialBoard();
        testCustomBoard();
        testMidGameBoard();
        System.out.println("All tests passed!");
    }

    static void testMidGameBoard() {
        String fen = "r1bqk2r/1pp1bppp/p1n2n2/4p3/B3P3/5N2/PPPP1PPP/RNBQ1RK1 w KQkq - 4 6";

        // FEN -> Board
        Board board = FenConvertor.fenToBoard(fen);

        System.out.println("Board JSON:");
        System.out.println(board.toJson());

        // Board -> FEN (pass whiteToMove from FEN)
        boolean whiteToMove = fen.split(" ")[1].equals("w");
        String fen2 = FenConvertor.boardToFen(board, whiteToMove);

        System.out.println("FEN from Board:");
        System.out.println(fen2);

        // For reversibility (compare only piece placement)
        assert fen2.startsWith(fen.split(" ")[0]) : "Mid-game board conversion failed!";
        System.out.println("Mid-game test passed!");
    }

    static void testInitialBoard() {
        Board board = new Board();
        board.initialize();

        // Board -> FEN (white to move initially)
        String fen = FenConvertor.boardToFen(board, true);
        System.out.println("Initial FEN: " + fen);
        // Expect: rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

        // FEN -> Board
        Board loadedBoard = FenConvertor.fenToBoard(fen);

        String fen2 = FenConvertor.boardToFen(loadedBoard, true);
        assert fen.equals(fen2) : "Initial board FEN conversion failed!";
    }

    static void testCustomBoard() {
        Board board = new Board();
        board.initialize();

        board.move("e2", "e4"); // pawn
        board.move("g1", "f3"); // knight

        // Assume white's turn after these moves (or track actual turn)
        boolean whiteToMove = true;

        String fen = FenConvertor.boardToFen(board, whiteToMove);
        System.out.println("Custom FEN: " + fen);

        Board loadedBoard = FenConvertor.fenToBoard(fen);
        String fen2 = FenConvertor.boardToFen(loadedBoard, whiteToMove);

        assert fen.equals(fen2) : "Custom board FEN conversion failed!";
    }
}
