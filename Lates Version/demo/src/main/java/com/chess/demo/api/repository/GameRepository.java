package com.chess.demo.api.repository;

import com.chess.demo.api.domain.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {
}