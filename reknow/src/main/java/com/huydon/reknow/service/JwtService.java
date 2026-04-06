package com.huydon.reknow.service;

import com.huydon.reknow.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret-key}") //đọc giá trị cấu hình
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    //1. tạo token cho client
    public String generateToken(User user ){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        return Jwts.builder() //Jwts tạo jwt và parse jwt
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis())) // lấy thời gian hiện tại
                .setExpiration(new Date(System.currentTimeMillis() + expiration) )
                .signWith(getSignKey())
                .compact();
    }

    //2. server mở token
    public Claims  extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)// decode token, verify token
                .getBody();


    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }


    public boolean isTokenValid(String token){
        try{
            extractAllClaims(token);
            return true;
        }catch (Exception e){
            return false; // token sai
        }

    }

    //convert secretKey-String sang dạng object cần để ký và verify
    public Key getSignKey(){

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}
