package com.chess.demo.api.misc;

import com.chess.demo.api.domain.enums.ChessPiece;
import com.github.bhlangonijr.chesslib.Piece;

public final class ChessPieceMapper {

    private ChessPieceMapper() {}

    public static Piece toChesslib(ChessPiece cp) {
        return switch (cp) {
            case KING_WHITE -> Piece.WHITE_KING;
            case KING_BLACK -> Piece.BLACK_KING;
            case QUEEN_WHITE -> Piece.WHITE_QUEEN;
            case QUEEN_BLACK -> Piece.BLACK_QUEEN;
            case ROOK_WHITE_QUEENSIDE, ROOK_WHITE_KINGSIDE -> Piece.WHITE_ROOK;
            case ROOK_BLACK_QUEENSIDE, ROOK_BLACK_KINGSIDE -> Piece.BLACK_ROOK;
            case BISHOP_WHITE_QUEENSIDE, BISHOP_WHITE_KINGSIDE -> Piece.WHITE_BISHOP;
            case BISHOP_BLACK_QUEENSIDE, BISHOP_BLACK_KINGSIDE -> Piece.BLACK_BISHOP;
            case KNIGHT_WHITE_QUEENSIDE, KNIGHT_WHITE_KINGSIDE -> Piece.WHITE_KNIGHT;
            case KNIGHT_BLACK_QUEENSIDE, KNIGHT_BLACK_KINGSIDE -> Piece.BLACK_KNIGHT;
            case PAWN_WHITE_A, PAWN_WHITE_B, PAWN_WHITE_C, PAWN_WHITE_D,
                 PAWN_WHITE_E, PAWN_WHITE_F, PAWN_WHITE_G, PAWN_WHITE_H -> Piece.WHITE_PAWN;
            case PAWN_BLACK_A, PAWN_BLACK_B, PAWN_BLACK_C, PAWN_BLACK_D,
                 PAWN_BLACK_E, PAWN_BLACK_F, PAWN_BLACK_G, PAWN_BLACK_H -> Piece.BLACK_PAWN;
        };
    }

    public static ChessPiece fromChesslib(Piece p) {
        if (p == null || p == Piece.NONE) {
            return null;
        }

        return switch (p) {
            case WHITE_KING   -> ChessPiece.KING_WHITE;
            case BLACK_KING   -> ChessPiece.KING_BLACK;
            case WHITE_QUEEN  -> ChessPiece.QUEEN_WHITE;
            case BLACK_QUEEN  -> ChessPiece.QUEEN_BLACK;
            case WHITE_ROOK   -> ChessPiece.ROOK_WHITE_KINGSIDE;
            case BLACK_ROOK   -> ChessPiece.ROOK_BLACK_KINGSIDE;
            case WHITE_BISHOP -> ChessPiece.BISHOP_WHITE_KINGSIDE;
            case BLACK_BISHOP -> ChessPiece.BISHOP_BLACK_KINGSIDE;
            case WHITE_KNIGHT -> ChessPiece.KNIGHT_WHITE_KINGSIDE;
            case BLACK_KNIGHT -> ChessPiece.KNIGHT_BLACK_KINGSIDE;
            case WHITE_PAWN   -> ChessPiece.PAWN_WHITE_A;
            case BLACK_PAWN   -> ChessPiece.PAWN_BLACK_A;

            default -> throw new IllegalArgumentException("Unexpected Piece value: " + p);
        };
    }
}