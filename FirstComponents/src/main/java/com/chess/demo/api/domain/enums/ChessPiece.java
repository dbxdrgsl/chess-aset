package com.chess.demo.api.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum ChessPiece {
    // White pieces
    PAWN_WHITE_A("Pawn", "White"), PAWN_WHITE_B("Pawn", "White"),
    PAWN_WHITE_C("Pawn", "White"), PAWN_WHITE_D("Pawn", "White"),
    PAWN_WHITE_E("Pawn", "White"), PAWN_WHITE_F("Pawn", "White"),
    PAWN_WHITE_G("Pawn", "White"), PAWN_WHITE_H("Pawn", "White"),

    ROOK_WHITE_QUEENSIDE("Rook", "White"),   // a1
    ROOK_WHITE_KINGSIDE("Rook", "White"),    // h1
    KNIGHT_WHITE_QUEENSIDE("Knight", "White"), // b1
    KNIGHT_WHITE_KINGSIDE("Knight", "White"),  // g1
    BISHOP_WHITE_QUEENSIDE("Bishop", "White"), // c1
    BISHOP_WHITE_KINGSIDE("Bishop", "White"),  // f1
    QUEEN_WHITE("Queen", "White"),
    KING_WHITE("King", "White"),

    // Black pieces
    PAWN_BLACK_A("Pawn", "Black"), PAWN_BLACK_B("Pawn", "Black"),
    PAWN_BLACK_C("Pawn", "Black"), PAWN_BLACK_D("Pawn", "Black"),
    PAWN_BLACK_E("Pawn", "Black"), PAWN_BLACK_F("Pawn", "Black"),
    PAWN_BLACK_G("Pawn", "Black"), PAWN_BLACK_H("Pawn", "Black"),

    ROOK_BLACK_QUEENSIDE("Rook", "Black"),
    ROOK_BLACK_KINGSIDE("Rook", "Black"),
    KNIGHT_BLACK_QUEENSIDE("Knight", "Black"),
    KNIGHT_BLACK_KINGSIDE("Knight", "Black"),
    BISHOP_BLACK_QUEENSIDE("Bishop", "Black"),
    BISHOP_BLACK_KINGSIDE("Bishop", "Black"),
    QUEEN_BLACK("Queen", "Black"),
    KING_BLACK("King", "Black");

    @Getter
    private final String type;

    @Getter
    private final String color;

    @Getter
    private boolean hasMoved = false;

    ChessPiece(String type, String color) {
        this.type = type;
        this.color = color;
    }

    public ChessPiece withMove() {
        this.hasMoved = true;
        return this;
    }

    public boolean isWhite() { return "White".equals(color); }
    public boolean isBlack() { return "Black".equals(color); }

    @JsonValue
    public String getCode() {
        return name();
    }

    public static ChessPiece fromCode(String code) {
        return valueOf(code);
    }
}