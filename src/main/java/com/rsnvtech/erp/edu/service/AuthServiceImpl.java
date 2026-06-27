package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;
import com.rsnvtech.erp.edu.repository.UserLoginAuditRepository;
import com.rsnvtech.erp.edu.repository.UserLoginRepository;
import com.rsnvtech.erp.edu.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private EmailService emailService;


    @Autowired
    private UserLoginRepository userLoginRepository;
    @Autowired
    private UserLoginAuditRepository userLoginAuditRepository;

    @Override
    public AuthTokenResponse login(AuthRequest request) {
        AuthTokenResponse authTokenResponse;

            return null;


    }

    @Override
    public void logout(AuthRequest request) {

    }

    @Override
    public void forgotPassword(AuthRequest request) {

    }

    @Override
    public void register(AuthRequest request) {

    }





}
