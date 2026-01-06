package com.chess.demo.api.service;

import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public Optional<Player> findById(Long id) {
        return playerRepository.findById(id);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findByEmail(String email) {
        return playerRepository.findPlayerByEmail(email);
    }
    
    public Player update(Long id, Player updatedPlayer) {
        return playerRepository.findById(id)
                .map(existingPlayer -> {
                    existingPlayer.setName(updatedPlayer.getName());
                    existingPlayer.setEmail(updatedPlayer.getEmail());
                    return playerRepository.save(existingPlayer);
                })
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + id));
    }

    public Player patch(Long id, Player updatedFields) {
        return playerRepository.findById(id)
                .map(existing -> {
                    if (updatedFields.getName() != null) existing.setName(updatedFields.getName());
                    if (updatedFields.getEmail() != null) existing.setEmail(updatedFields.getEmail());
                    return playerRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Player not found with id: " + id));
    }

    public boolean deleteById(Long id) {
        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
