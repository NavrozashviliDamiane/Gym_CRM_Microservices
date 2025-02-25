package com.epam.crmgym.config;

import com.epam.crmgym.entity.User;

import com.epam.crmgym.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long expirationMinutes;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;

    private Map<String, Boolean> blacklistedTokens = new ConcurrentHashMap<>();




    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String generateToken(User user, Map<String, Object> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (expirationMinutes * 60 * 1000));
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key generateKey(){
        byte[] secreateAsBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secreateAsBytes);
    }


    public void blacklistToken(String token) {
        blacklistedTokens.put(token, true);
    }

    public boolean isTokenBlacklisted(String token) {
        log.info("JWT service blocked token:" + token);
        log.info("JWT Service List of the blocklisted" + blacklistedTokens);
        return blacklistedTokens.containsKey(token);
    }


    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser().setSigningKey(secretKey).build()
                .parseClaimsJws(jwt).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);

            Date expirationDate = claims.getBody().getExpiration();
            return expirationDate.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

}