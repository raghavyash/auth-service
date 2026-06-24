package com.rsnvtech.erp.edu.entity;

import com.rsnvtech.erp.edu.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "user_login")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserLogin  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;
    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String userEmail;
    @Column(name = "USER_PASSWORD")
    private String userPassword;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "IS_VALID")
    private Boolean isValid;  // login
    @Column(name = "USER_AGENT")
    private String userAgent;// Browser and OS details

    @Column(name = "EXPIRY_DATE", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;




}
