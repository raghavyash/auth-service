package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.config.JwtService;
import com.rsnvtech.erp.edu.constants.Role;
import com.rsnvtech.erp.edu.entity.UserLogin;
import com.rsnvtech.erp.edu.entity.UserLoginAudit;
import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;
import com.rsnvtech.erp.edu.repository.UserLoginAuditRepository;
import com.rsnvtech.erp.edu.repository.UserLoginRepository;
import com.rsnvtech.erp.edu.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private UserLoginAuditRepository userLoginAuditRepository;

    @Override
    public AuthTokenResponse login(AuthRequest request) {
        AuthTokenResponse authTokenResponse;
        UserLogin u= userLoginRepository.findByUserEmail(request.getUserEmail());
        if (u!= null) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserEmail(), request.getPwd())
            );

            // user = userLoginRepository.findByUserEmail(request.getUserEmail());
            var jwtToken = jwtService.generateAccessToken(u);
            return new AuthTokenResponse(jwtToken);
        } else {
            throw new IllegalArgumentException("Email already registered");
        }

    }

    @Override
    public void logout(AuthRequest request) {

    }

    @Override
    public void forgotPassword(AuthRequest request) {

    }

    @Override
    public void register(AuthRequest request) {
       UserLogin u= userLoginRepository.findByUserEmail(request.getUserEmail());
        if (u!= null) {
            throw new IllegalArgumentException("Email already registered");
        }
        String token = UUID.randomUUID().toString();
        var userLogin = UserLogin.builder()
                .userEmail(request.getUserEmail())
                .userPassword(request.getPwd()).token(token)
                .role(Role.USER).createDate(LocalDateTime.now()).expiryDate(LocalDateTime.now())
                 // Defaults new registrations to standard USER
                .isValid(false)  // Keep disabled until email is verified
                .build();
        userLoginRepository.save(userLogin);
       var userLoginAudit = UserLoginAudit.builder().userLogin(userLogin).userEmail(userLogin.getUserEmail())
                       .status("Active").createDate(LocalDateTime.now()).build();
       userLoginAuditRepository.save(userLoginAudit);
        emailService.sendVerificationEmail(userLogin.getUserEmail(), token);
    }



    private  AuthTokenResponse authenticate(AuthRequest request) {
        // Automatically checks if user is enabled; throws exception if disabled
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserEmail(), request.getPwd())
        );

        var user = userLoginRepository.findByUserEmail(request.getUserEmail());
        var jwtToken = jwtService.generateAccessToken(user);
        return new AuthTokenResponse(jwtToken);
    }

}
