package com.rsnvtech.erp.edu.controller;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.entity.Token;
import com.rsnvtech.erp.edu.entity.User;
import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthResponse;
import com.rsnvtech.erp.edu.model.RefreshTokenRequest;
import com.rsnvtech.erp.edu.model.RegisterRequest;
import com.rsnvtech.erp.edu.service.AuthenticationService;
import com.rsnvtech.erp.edu.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody AuthRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody RefreshTokenRequest request) {

        Token token = refreshTokenService.verifyToken(request.getRefreshToken());
        if (token != null) {
            refreshTokenService.revokeToken(token);
            return "Logged out successfully";
        }
        return "Not Logged out successfully";
    }

   /* @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        Token refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        refreshTokenService.revokeToken(refreshToken);

        Token newRefreshToken = refreshTokenService.saveRefreshToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build()
        );
    }*/
}
