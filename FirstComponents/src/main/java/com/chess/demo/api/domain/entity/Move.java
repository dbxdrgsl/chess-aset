package com.chess.demo.api.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "moves")
@Getter
@Setter
@NoArgsConstructor
public class Move {

    @Id
    private String moveId = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private int moveNumber;

    @Column(nullable = false)
    private String from;

    @Column(nullable = false)
    private String to;

    private String promotion;

    private String capturedPiece;

    private String san;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Optional: special move flags
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isCastling;
    private boolean isEnPassant;
}