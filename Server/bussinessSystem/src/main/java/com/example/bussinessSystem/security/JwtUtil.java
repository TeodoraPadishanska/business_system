package com.example.bussinessSystem.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:UsRw95Jwm5cHAvJvOZv76s6jxn5nIpNLHo3awkjFo4k}")
    private String SECRET;
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            Jwts.parser().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }
}
