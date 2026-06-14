package com.rsnvtech.erp.edu.service;

import com.rsnvtech.erp.edu.model.AuthRequest;
import com.rsnvtech.erp.edu.model.AuthTokenResponse;

public interface AuthService {
    AuthTokenResponse login(AuthRequest request);
    void logout(AuthRequest request);
    void forgotPassword(AuthRequest request);
    void register(AuthRequest request);

}
