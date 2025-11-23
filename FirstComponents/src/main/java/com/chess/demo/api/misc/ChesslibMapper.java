package com.chess.demo.api.misc;

import com.chess.demo.api.domain.entity.Board;
import com.chess.demo.api.domain.enums.ChessPiece;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

public class ChesslibMapper {

    public static String toFEN(Board myBoard, boolean whiteToMove,
                               String castlingRights, String enPassant) {

        StringBuilder fen = new StringBuilder();

        for (int rank = 8; rank >= 1; rank--) {
            int empty = 0;

            for (char file = 'a'; file <= 'h'; file++) {
                String square = "" + file + rank;
                ChessPiece p = myBoard.get(square);

                if (p == null) {
                    empty++;
                    continue;
                }

                if (empty > 0) {
                    fen.append(empty);
                    empty = 0;
                }

                fen.append(toFenChar(p));
            }

            if (empty > 0) fen.append(empty);
            if (rank > 1) fen.append('/');
        }

        // side to move
        fen.append(' ');
        fen.append(whiteToMove ? 'w' : 'b');

        // castling
        fen.append(' ');
        fen.append(castlingRights.isEmpty() ? "-" : castlingRights);

        // en passant
        fen.append(' ');
        fen.append(enPassant != null ? enPassant : "-");

        // Halfmove & fullmove default
        fen.append(" 0 1");

        return fen.toString();
    }

    private static char toFenChar(ChessPiece p) {
        char c = switch (p.getType()) {
            case "Pawn" -> 'p';
            case "Knight" -> 'n';
            case "Bishop" -> 'b';
            case "Rook" -> 'r';
            case "Queen" -> 'q';
            case "King" -> 'k';
            default -> '?';
        };
        return p.isWhite() ? Character.toUpperCase(c) : c;
    }
}

