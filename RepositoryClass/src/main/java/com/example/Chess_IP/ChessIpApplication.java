package com.example.Chess_IP;

import com.example.Chess_IP.domain.Game;
import com.example.Chess_IP.domain.Move;
import com.example.Chess_IP.domain.Player;
import com.example.Chess_IP.repository.GameRepository;
import com.example.Chess_IP.repository.MoveRepository;
import com.example.Chess_IP.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChessIpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessIpApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(PlayerRepository playerRepository,
                                   GameRepository gameRepository,
                                   MoveRepository moveRepository) {
        return args -> {
            if (playerRepository.count() == 0) {
                Player alice = new Player("11111111-1111-1111-1111-111111111111", "Alice", false);
                Player bob = new Player("22222222-2222-2222-2222-222222222222", "Bob", false);
                playerRepository.save(alice);
                playerRepository.save(bob);

                Game game = new Game("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                        alice, bob, null, null, "IN_PROGRESS");
                gameRepository.save(game);

                Move move = new Move("11111111-1111-1111-1111-111111111111",
                        game, alice, "e2", "e4", "Pawn", null, "2025-10-26T22:00:00");
                moveRepository.save(move);

                System.out.println("=== Players ===");
                playerRepository.findAll().forEach(System.out::println);

                System.out.println("=== Games ===");
                gameRepository.findAll().forEach(System.out::println);

                System.out.println("=== Moves ===");
                moveRepository.findAll().forEach(System.out::println);
            }
        };
    }
}
