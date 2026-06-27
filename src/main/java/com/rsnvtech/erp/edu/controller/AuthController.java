package com.rsnvtech.erp.edu.controller;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.constants.Role;
import com.rsnvtech.erp.edu.entity.User;
import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthResponse;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;
import com.rsnvtech.erp.edu.model.RegisterRequest;
import com.rsnvtech.erp.edu.repository.UserRepository;
import com.rsnvtech.erp.edu.service.AuthService;
import com.rsnvtech.erp.edu.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
   /* private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;*/
    private final AuthenticationService service;

    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request) {

        return service.authenticate(request);
    }
   /* @PostMapping("/register")
    public String register(
            @RequestBody AuthRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        return "User registered successfully";
    }*/

    /*@PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                ));

        String token =
                jwtService.generateToken(
                        request.getUsername());

        return new AuthResponse(token);
    }*/


    }
