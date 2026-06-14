package com.rsnvtech.erp.edu.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Data
public class AuthRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String userEmail;
    private String pwd;

}
