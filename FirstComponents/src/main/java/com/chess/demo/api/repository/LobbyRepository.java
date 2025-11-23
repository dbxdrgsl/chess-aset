package com.chess.demo.api.repository;

import com.chess.demo.api.domain.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    @Query("SELECT l FROM Lobby l WHERE l.status = 'WAITING' ORDER BY l.createdAt DESC LIMIT 1")
    Optional<Lobby> findLatestWaitingLobby();

    Optional<Lobby> findFirstByStatusOrderByCreatedAtDesc(Lobby.Status status);
}