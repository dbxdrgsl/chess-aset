package com.chess.demo.api.repository;

import com.chess.demo.api.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT s FROM Player s WHERE s.email = :email")
    Player findPlayerByEmail(String email);

}
