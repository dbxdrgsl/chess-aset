package com.example.Chess_IP.service;

import com.example.Chess_IP.domain.Game;
import com.example.Chess_IP.domain.Move;

public interface GameService {
    void processMove(String gameId, Move move);
    Game getGameState(String gameId);
}