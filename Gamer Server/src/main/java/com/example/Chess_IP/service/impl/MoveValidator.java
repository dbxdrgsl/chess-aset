package com.example.Chess_IP.service.impl;

import com.example.Chess_IP.domain.Game;
import com.example.Chess_IP.domain.Move;

public class MoveValidator {

    public void validateMove(Move move, Game game) {
        String from = move.getFromPosition();
        String to = move.getToPosition();

        // Board boundaries
        if (!from.matches("[a-h][1-8]") || !to.matches("[a-h][1-8]")) {
            throw new RuntimeException("Move is outside of the board");
        }

        // Player turn
        if (game.getLastPlayerMove() != null && game.getLastPlayerMove().equals(move.getPlayer())) {
            throw new RuntimeException("It is not your turn");
        }

        // Coordinates
        int fromCol = from.charAt(0) - 'a';
        int toCol = to.charAt(0) - 'a';
        int fromRow = Character.getNumericValue(from.charAt(1));
        int toRow = Character.getNumericValue(to.charAt(1));
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        String piece = move.getPieceMoved().toLowerCase();

        switch (piece) {
            case "pawn":
                validatePawnMove(move, fromRow, toRow, fromCol, toCol);
                break;
            case "rook":
                if (rowDiff != 0 && colDiff != 0)
                    throw new RuntimeException("Invalid rook move: must move straight");
                break;
            case "bishop":
                if (rowDiff != colDiff)
                    throw new RuntimeException("Invalid bishop move: must move diagonally");
                break;
            case "queen":
                if (!(rowDiff == colDiff || rowDiff == 0 || colDiff == 0))
                    throw new RuntimeException("Invalid queen move: must move straight or diagonal");
                break;
            case "knight":
                if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)))
                    throw new RuntimeException("Invalid knight move: must move in L-shape");
                break;
            case "king":
                if (rowDiff > 1 || colDiff > 1)
                    throw new RuntimeException("Invalid king move: must move one square");
                break;
            default:
                throw new RuntimeException("Unknown piece type: " + piece);
        }
    }

    private void validatePawnMove(Move move, int fromRow, int toRow, int fromCol, int toCol) {
        boolean sameColumn = fromCol == toCol;
        int rowDiff = toRow - fromRow;
        boolean isWhite = "player1".equalsIgnoreCase(move.getPlayer().getPlayerId());

        if (isWhite) {
            if (rowDiff <= 0) throw new RuntimeException("Invalid pawn move: white must move forward");
            if (!sameColumn && Math.abs(fromCol - toCol) != 1)
                throw new RuntimeException("Invalid pawn capture movement");
            if (sameColumn && !(rowDiff == 1 || (fromRow == 2 && rowDiff == 2)))
                throw new RuntimeException("Invalid pawn move distance");
        } else {
            if (rowDiff >= 0) throw new RuntimeException("Invalid pawn move: black must move forward");
            if (!sameColumn && Math.abs(fromCol - toCol) != 1)
                throw new RuntimeException("Invalid pawn capture movement");
            if (sameColumn && !(rowDiff == -1 || (fromRow == 7 && rowDiff == -2)))
                throw new RuntimeException("Invalid pawn move distance");
        }
    }
}
