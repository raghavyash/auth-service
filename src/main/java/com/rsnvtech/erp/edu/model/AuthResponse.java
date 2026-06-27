package com.rsnvtech.erp.edu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;

}
