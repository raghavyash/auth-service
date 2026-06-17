package com.rsnvtech.erp.edu.entity;

import com.rsnvtech.erp.edu.constants.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "user_login")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserLogin implements UserDetails {

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() { return userEmail; }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


}
