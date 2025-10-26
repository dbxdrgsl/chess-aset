package com.example.Chess_IP.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GAME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {

    @Id
    @Column(name = "GAME_ID")
    private String gameId;

    @ManyToOne
    @JoinColumn(name = "WHITE_PLAYER_ID", nullable = false)
    private Player whitePlayer;

    @ManyToOne
    @JoinColumn(name = "BLACK_PLAYER_ID", nullable = false)
    private Player blackPlayer;

    @Column(name = "LAST_MOVE")
    private String lastMove;

    @ManyToOne
    @JoinColumn(name = "LAST_PLAYER_MOVE_ID")
    private Player lastPlayerMove;

    @Column(name = "GAME_STATE", nullable = false)
    private String gameState;
}
