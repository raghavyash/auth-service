package com.rsnvtech.erp.edu.model;

import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String username;
    @Size(min = 6)
    private String password;


}
