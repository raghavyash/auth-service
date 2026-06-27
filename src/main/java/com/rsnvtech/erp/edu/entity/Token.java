package com.rsnvtech.erp.edu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000,unique = true, nullable = false)
    private String token;

    private boolean revoked;

    private boolean expired;
    private Instant expiryDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
