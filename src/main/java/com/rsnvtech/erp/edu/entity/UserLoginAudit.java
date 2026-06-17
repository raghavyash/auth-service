package com.rsnvtech.erp.edu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_login_audit")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserLoginAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String userEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_LOGIN_ID")
    private UserLogin userLogin;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "STATUS")
    private String status;  //  'success', 'failed_password', 'user_not_found', 'suspended'
    @Column(name = "USER_AGENT")
    private String userAgent;// Browser and OS details

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

}
