package com.chess.demo.api.domain.entity;

import com.chess.demo.api.domain.enums.ChessPiece;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Board {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, ChessPiece> positions = new HashMap<>(64);

    public ChessPiece get(String square) { // e.g. "e4"
        return positions.get(square);
    }

    public void place(String square, ChessPiece piece) {
        positions.put(square, piece);
    }

    public ChessPiece remove(String square) {
        return positions.remove(square);
    }

    /** Returns captured piece (or null) */
    public ChessPiece move(String from, String to) {
        ChessPiece piece = get(from);
        if (piece == null) throw new IllegalArgumentException("No piece at " + from);

        ChessPiece captured = remove(to);
        remove(from);
        place(to, piece.withMove()); // mark as moved

        return captured;
    }

    public void initialize() {
        positions.clear();

        // White back rank
        place("a1", ChessPiece.ROOK_WHITE_QUEENSIDE);
        place("b1", ChessPiece.KNIGHT_WHITE_QUEENSIDE);
        place("c1", ChessPiece.BISHOP_WHITE_QUEENSIDE);
        place("d1", ChessPiece.QUEEN_WHITE);
        place("e1", ChessPiece.KING_WHITE);
        place("f1", ChessPiece.BISHOP_WHITE_KINGSIDE);
        place("g1", ChessPiece.KNIGHT_WHITE_KINGSIDE);
        place("h1", ChessPiece.ROOK_WHITE_KINGSIDE);

        // White pawns
        for (char file = 'a'; file <= 'h'; file++) {
            place(file + "2", ChessPiece.valueOf("PAWN_WHITE_" + Character.toUpperCase(file)));
        }

        // Black back rank
        place("a8", ChessPiece.ROOK_BLACK_QUEENSIDE);
        place("b8", ChessPiece.KNIGHT_BLACK_QUEENSIDE);
        place("c8", ChessPiece.BISHOP_BLACK_QUEENSIDE);
        place("d8", ChessPiece.QUEEN_BLACK);
        place("e8", ChessPiece.KING_BLACK);
        place("f8", ChessPiece.BISHOP_BLACK_KINGSIDE);
        place("g8", ChessPiece.KNIGHT_BLACK_KINGSIDE);
        place("h8", ChessPiece.ROOK_BLACK_KINGSIDE);

        // Black pawns
        for (char file = 'a'; file <= 'h'; file++) {
            place(file + "7", ChessPiece.valueOf("PAWN_BLACK_" + Character.toUpperCase(file)));
        }
    }

    // JSON serialization helpers
    public String toJson() {
        try {
            Map<String, String> stringMap = new HashMap<>();
            positions.forEach((sq, piece) -> stringMap.put(sq, piece.name()));
            return mapper.writeValueAsString(stringMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Board fromJson(String json) {
        Board board = new Board();
        if (json == null || json.isBlank()) {
            board.initialize();
            return board;
        }
        try {
            Map<String, String> map = mapper.readValue(json, new TypeReference<>() {});
            for (var e : map.entrySet()) {
                board.place(e.getKey(), ChessPiece.valueOf(e.getValue()));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load board", ex);
        }
        return board;
    }

}