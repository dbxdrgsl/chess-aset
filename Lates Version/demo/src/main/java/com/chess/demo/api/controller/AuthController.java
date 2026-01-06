package com.chess.demo.api.controller;

import com.chess.demo.api.domain.entity.Player;
import com.chess.demo.api.domain.entity.UserEntity;
import com.chess.demo.api.domain.enums.Role;
import com.chess.demo.api.dto.auth.AuthRequestDto;
import com.chess.demo.api.dto.auth.AuthResponseDto;
import com.chess.demo.api.dto.auth.RegisterDto;
import com.chess.demo.api.repository.UserRepository;
import com.chess.demo.api.security.JwtService;
import com.chess.demo.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) throws Exception {
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwt = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDto(jwt, user.getEmail(), user.getRole(), user.getId()));
    }

    @PostMapping("/register/player")
    @Transactional
    public ResponseEntity<?> registerPlayer(@RequestBody RegisterDto request)
    {
        if(userRepository.existsByEmail(request.email())){
            return  ResponseEntity.badRequest().body("Email is already registered");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.email());
        userEntity.setPassword(passwordEncoder.encode(request.password()));
        userEntity.setRole(Role.PLAYER);
        userEntity = userRepository.save(userEntity);

        Player player = new Player();
        player.setEmail(request.email());
        player.setName(request.name());
        player.setUser(userEntity);
        player = playerService.save(player);


        String jwt = jwtService.generateToken(userEntity);
        return  ResponseEntity.ok(new AuthResponseDto(jwt, userEntity.getEmail(), userEntity.getRole(), userEntity.getId()));
    }
}
