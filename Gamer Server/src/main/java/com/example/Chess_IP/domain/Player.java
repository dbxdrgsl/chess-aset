package com.example.Chess_IP.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PLAYER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Player {

    @Id
    @Column(name = "PLAYER_ID")
    private String playerId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IS_BOT", nullable = false)
    private boolean isBot;
}
