package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.entity.UserLogin;
import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;
import com.rsnvtech.erp.edu.repository.UserLoginRepository;
import com.rsnvtech.erp.edu.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Override
    public AuthTokenResponse login(AuthRequest request) {
        AuthTokenResponse authTokenResponse;
        if (!userLoginRepository.findByUserEmail(request.getUserEmail()).getIsValid()) {
            authTokenResponse = authenticate(request);
        } else {
            throw new IllegalArgumentException("Email already registered");
        }
        return authTokenResponse;
    }

    @Override
    public void logout(AuthRequest request) {

    }

    @Override
    public void forgotPassword(AuthRequest request) {

    }

    @Override
    public void register(AuthRequest request) {
        if (userLoginRepository.findByUserEmail(request.getUserEmail()).getIsValid()) {
            throw new IllegalArgumentException("Email already registered");
        }

        var user = UserLogin.builder()
                .userEmail(request.getUserEmail())
                .userPassword(request.getPwd())

                 // Defaults new registrations to standard USER
                .isValid(false)  // Keep disabled until email is verified
                .build();
        userLoginRepository.save(user);
        String token = UUID.randomUUID().toString();
        emailService.sendVerificationEmail(user.getUserEmail(), token);
    }



    public AuthTokenResponse authenticate(AuthRequest request) {
        // Automatically checks if user is enabled; throws exception if disabled
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserEmail(), request.getPwd())
        );

        var user = userLoginRepository.findByUserEmail(request.getUserEmail());
        var jwtToken = jwtService.generateAccessToken(user);
        return new AuthTokenResponse(jwtToken);
    }

}
