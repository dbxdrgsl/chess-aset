package com.example.Chess_IP.repository;

import com.example.Chess_IP.domain.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, String> {
}
