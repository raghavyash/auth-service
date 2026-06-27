package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.constants.Role;
import com.rsnvtech.erp.edu.entity.Token;
import com.rsnvtech.erp.edu.entity.User;
import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthResponse;
import com.rsnvtech.erp.edu.model.RegisterRequest;
import com.rsnvtech.erp.edu.repository.TokenRepository;
import com.rsnvtech.erp.edu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authManager;

    public AuthResponse register(
            RegisterRequest request) {

        User user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String accessToken =
                jwtService.generateToken(
                        user.getUsername());

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUsername());

        saveToken(user, accessToken);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(
            AuthRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken =
                jwtService.generateToken(
                        user.getUsername());

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getUsername());

        revokeAllTokens(user);

        saveToken(user, accessToken);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveToken(
            User user,
            String jwtToken) {

        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllTokens(User user) {

        var validTokens =
                tokenRepository.findAll();

        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validTokens);
    }
}
