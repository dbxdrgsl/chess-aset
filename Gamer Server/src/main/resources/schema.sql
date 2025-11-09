CREATE TABLE Player (
                        player_id VARCHAR(36) PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        is_bot BOOLEAN NOT NULL
);

CREATE TABLE Game (
                      game_id VARCHAR(36) PRIMARY KEY,
                      white_player_id VARCHAR(36) NOT NULL,
                      black_player_id VARCHAR(36) NOT NULL,
                      last_move VARCHAR(10),
                      last_player_move_id VARCHAR(36),
                      game_state VARCHAR(20) NOT NULL,
                      CONSTRAINT fk_white_player
                          FOREIGN KEY (white_player_id) REFERENCES Player(player_id),
                      CONSTRAINT fk_black_player
                          FOREIGN KEY (black_player_id) REFERENCES Player(player_id),
                      CONSTRAINT fk_last_player
                          FOREIGN KEY (last_player_move_id) REFERENCES Player(player_id)
);

CREATE TABLE Move (
                      move_id VARCHAR(36) PRIMARY KEY,
                      game_id VARCHAR(36) NOT NULL,
                      player_id VARCHAR(36) NOT NULL,
                      from_position VARCHAR(2) NOT NULL,
                      to_position VARCHAR(2) NOT NULL,
                      piece_moved VARCHAR(10) NOT NULL,
                      piece_captured VARCHAR(10),
                      move_timestamp TIMESTAMP,
                      CONSTRAINT fk_move_game
                          FOREIGN KEY (game_id) REFERENCES Game(game_id),
                      CONSTRAINT fk_move_player
                          FOREIGN KEY (player_id) REFERENCES Player(player_id)
);
