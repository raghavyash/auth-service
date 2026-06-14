package com.rsnvtech.erp.edu.controller;

import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthResponse;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;
import com.rsnvtech.erp.edu.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@RequestBody @Valid AuthRequest request) {
        AuthTokenResponse authTokenResponse= authService.login(request);
        return ResponseEntity.ok(authTokenResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new AuthResponse("Registration successful! Check your email for verification.", HttpStatus.OK.toString()));
    }


}
