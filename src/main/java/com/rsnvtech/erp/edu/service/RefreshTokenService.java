package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.entity.Token;
import com.rsnvtech.erp.edu.entity.User;
import com.rsnvtech.erp.edu.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private TokenRepository tokenRepository;
   @Autowired
   private JwtService jwtService;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDuration;


    public Token saveRefreshToken(User user) {

        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtService.generateRefreshToken(user));
        token.setExpiryDate(
                Instant.now().plusMillis(refreshTokenDuration)
        );
        token.setRevoked(false);

        return tokenRepository.save(token);
    }

    public Token verifyToken(String token) {

        Token refreshToken = tokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            tokenRepository.delete(refreshToken);
            throw new RuntimeException("Token expired");
        }

        return refreshToken;
    }

    public void revokeToken(Token token) {
        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
