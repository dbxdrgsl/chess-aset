package com.example.Chess_IP.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MOVE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Move {

    @Id
    @Column(name = "MOVE_ID")
    private String moveId;

    @ManyToOne
    @JoinColumn(name = "GAME_ID", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    private Player player;

    @Column(name = "FROM_POSITION", nullable = false)
    private String fromPosition;

    @Column(name = "TO_POSITION", nullable = false)
    private String toPosition;

    @Column(name = "PIECE_MOVED", nullable = false)
    private String pieceMoved;

    @Column(name = "PIECE_CAPTURED")
    private String pieceCaptured;

    @Column(name = "MOVE_TIMESTAMP")
    private String moveTimestamp;
}
