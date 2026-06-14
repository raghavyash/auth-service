package com.rsnvtech.erp.edu.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String responseMsg;
    private String responseCode;

    public AuthResponse(String responseMsg, String responseCode) {
    this.responseMsg=responseMsg;
    this.responseCode=responseCode;
    }
}
