package com.rsnvtech.erp.edu.config;

import com.rsnvtech.erp.edu.entity.UserLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${spring.application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.application.security.jwt.access-token-expiration}")
    private long jwtExpiration;

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a single claim using a functional interface resolver
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate Access Token with basic user details
    public String generateAccessToken(UserLogin userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Inject user roles/authorities into the JWT claims payload
        extraClaims.put("roles", userDetails.getRole().describeConstable().stream()
                .toList());
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Standard structural builder for JWT tokens (JJWT 0.12.x syntax)
    private String buildToken(Map<String, Object> extraClaims, UserLogin userDetails, long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUserEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Updated 0.12.x signature specifier
                .compact();
    }

    // Cryptographically validate the token signature and baseline expiration
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Parse claims safely using the application key
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Updated 0.12.x parsing validation
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Decodes key string to build a cryptographically sound SecretKey object
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
