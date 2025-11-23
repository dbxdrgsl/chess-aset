package com.chess.demo.api.repository;

import com.chess.demo.api.domain.entity.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<Move, String> {

}
