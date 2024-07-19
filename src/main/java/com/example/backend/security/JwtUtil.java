package com.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String secretKey;

    @Value("${jwt.secret}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        return generateTokenWithExpiry(username, 1000 * 60 * 60 * 10); // 10 hours
    }

    // Добавление метода для генерации рефреш токена
    public String generateRefreshToken(String username) {
        return generateTokenWithExpiry(username, 1000 * 60 * 60 * 24 * 7); // 7 days
    }

    // Факторизация генерации токена с настраиваемым временем истечения
    private String generateTokenWithExpiry(String username, long expirationMillis) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token) {
        final String usernameFromToken = extractUsername(token);
        return (!isTokenExpired(token));
    }
}
