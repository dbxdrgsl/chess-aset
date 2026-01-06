package com.chess.demo.api.misc;

import com.chess.demo.api.domain.entity.Board;
import com.chess.demo.api.domain.enums.ChessPiece;

public class FenConvertor {
    /**
     * Converts your Board to a FEN string
     * Currently assumes:
     *  - White to move
     *  - Castling KQkq available
     *  - No en-passant
     *  - Halfmove = 0, Fullmove = 1
     */
//    public static String boardToFen(Board board) {
//        StringBuilder fen = new StringBuilder();
//
//        // ranks 8 -> 1
//        for (int rank = 8; rank >= 1; rank--) {
//            int empty = 0;
//
//            for (char file = 'a'; file <= 'h'; file++) {
//                String square = "" + file + rank;
//                ChessPiece piece = board.get(square);
//
//                if (piece == null) {
//                    empty++;
//                } else {
//                    if (empty > 0) {
//                        fen.append(empty);
//                        empty = 0;
//                    }
//                    fen.append(toFenChar(piece));
//                }
//            }
//
//            if (empty > 0) fen.append(empty);
//            if (rank > 1) fen.append('/');
//        }
//
//        // Active color, castling, en-passant, half/fullmove
//        fen.append(" w KQkq - 0 1"); // TODO: update dynamically later
//
//        return fen.toString();
//    }

    public static String boardToFen(Board board, boolean whiteToMove) {
        StringBuilder fen = new StringBuilder();

        // ranks 8 -> 1
        for (int rank = 8; rank >= 1; rank--) {
            int empty = 0;

            for (char file = 'a'; file <= 'h'; file++) {
                String square = "" + file + rank;
                ChessPiece piece = board.get(square);

                if (piece == null) {
                    empty++;
                } else {
                    if (empty > 0) {
                        fen.append(empty);
                        empty = 0;
                    }
                    fen.append(toFenChar(piece));
                }
            }

            if (empty > 0) fen.append(empty);
            if (rank > 1) fen.append('/');
        }

        // Active color: 'w' or 'b'
        String activeColor = whiteToMove ? "w" : "b";

        // Keep castling rights, en-passant, half/full move for now
        fen.append(" ").append(activeColor).append(" KQkq - 0 1");

        return fen.toString();
    }


    private static char toFenChar(ChessPiece piece) {
        char c = switch (piece.getType()) {
            case "Pawn" -> 'p';
            case "Knight" -> 'n';
            case "Bishop" -> 'b';
            case "Rook" -> 'r';
            case "Queen" -> 'q';
            case "King" -> 'k';
            default -> '?';
        };
        return piece.isWhite() ? Character.toUpperCase(c) : c;
    }

    public static Board fenToBoard(String fen) {
        Board board = new Board();
        String[] parts = fen.split(" ");
        String[] ranks = parts[0].split("/");

        for (int i = 0; i < 8; i++) {
            String rank = ranks[i];
            int fileIndex = 0;

            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    fileIndex += c - '0';
                } else {
                    char file = (char) ('a' + fileIndex);
                    int rankNum = 8 - i;
                    String square = "" + file + rankNum;
                    board.place(square, fromFenChar(c, file, rankNum));
                    fileIndex++;
                }
            }
        }

        return board;
    }

    private static ChessPiece fromFenChar(char c, char file, int rank) {
        boolean isWhite = Character.isUpperCase(c);
        char lower = Character.toLowerCase(c);

        switch (lower) {
            case 'p':
                return isWhite ? ChessPiece.valueOf("PAWN_WHITE_" + Character.toUpperCase(file))
                        : ChessPiece.valueOf("PAWN_BLACK_" + Character.toUpperCase(file));
            case 'r':
                if (isWhite) return (file == 'a') ? ChessPiece.ROOK_WHITE_QUEENSIDE : ChessPiece.ROOK_WHITE_KINGSIDE;
                else return (file == 'a') ? ChessPiece.ROOK_BLACK_QUEENSIDE : ChessPiece.ROOK_BLACK_KINGSIDE;
            case 'n':
                if (isWhite) return (file == 'b') ? ChessPiece.KNIGHT_WHITE_QUEENSIDE : ChessPiece.KNIGHT_WHITE_KINGSIDE;
                else return (file == 'b') ? ChessPiece.KNIGHT_BLACK_QUEENSIDE : ChessPiece.KNIGHT_BLACK_KINGSIDE;
            case 'b':
                if (isWhite) return (file == 'c') ? ChessPiece.BISHOP_WHITE_QUEENSIDE : ChessPiece.BISHOP_WHITE_KINGSIDE;
                else return (file == 'c') ? ChessPiece.BISHOP_BLACK_QUEENSIDE : ChessPiece.BISHOP_BLACK_KINGSIDE;
            case 'q':
                return isWhite ? ChessPiece.QUEEN_WHITE : ChessPiece.QUEEN_BLACK;
            case 'k':
                return isWhite ? ChessPiece.KING_WHITE : ChessPiece.KING_BLACK;
            default:
                throw new IllegalArgumentException("Invalid FEN char: " + c);
        }
    }
}
