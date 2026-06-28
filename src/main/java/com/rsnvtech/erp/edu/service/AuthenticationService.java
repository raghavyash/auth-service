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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final AccountLockService accountLockService;
    @Autowired
    private  RefreshTokenService refreshTokenService;

    @Autowired
    private  JwtService jwtService;

    public String register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {

            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return "Successfully User Save";
    }

    public AuthResponse login(AuthRequest request) {


        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow();
        if (!user.isAccountNonLocked()) {
            boolean unlocked = accountLockService.unlockWhenTimeExpired(user);

            if (!unlocked) {
                throw new RuntimeException(
                        "Account locked. Try again later."
                );
            }
        }
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));

            String accessToken = jwtService.generateToken(user);
            Token refreshToken = refreshTokenService.saveRefreshToken(user);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build();

        } catch (BadCredentialsException ex) {
            accountLockService.increaseFailedAttempts(user);

            throw new RuntimeException("Invalid credentials");
        }


    }

}
