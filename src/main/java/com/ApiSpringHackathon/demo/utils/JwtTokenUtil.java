package com.ApiSpringHackathon.demo.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {



    private final Key secreteKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; //1 min

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secreteKey)
                .compact();
    }
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secreteKey)
                    .build()
                    .parseClaimsJws(token);
            return "valid";
        } catch (ExpiredJwtException ex) {
            // Token is expired
            return "expired token";
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid (failed parsing or verification)
            return "invalid token";
        }
    }

    public long getRemainingTime(String token) {
        try {
            Date expirationDate = Jwts.parserBuilder()
                    .setSigningKey(secreteKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expirationDate.getTime() - System.currentTimeMillis();
        } catch (ExpiredJwtException ex) {
            return 0; // Token is expired
        } catch (JwtException | IllegalArgumentException e) {
            return -1; // Token is invalid
        }
    }
}