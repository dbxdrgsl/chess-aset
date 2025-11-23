package com.chess.demo.api.domain.entity;

public record MoveResult(
        boolean legal,
        String message,           // e.g. "No piece on e2", "Puts king in check"
        String capturedPieceName, // e.g. "PAWN_BLACK"
        boolean isCastling,
        boolean isEnPassant
) {
    public static MoveResult illegal(String msg) {
        return new MoveResult(false, msg, null, false, false);
    }
    public static MoveResult legal(String captured) {
        return new MoveResult(true, null, captured, false, false);
    }
    public static MoveResult legalCastling() {
        return new MoveResult(true, null, null, true, false);
    }
    public static MoveResult legalEnPassant(String captured) {
        return new MoveResult(true, null, captured, false, true);
    }
}