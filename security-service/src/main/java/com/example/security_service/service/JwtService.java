package com.example.security_service.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    public static final String SECRET="7cbfae7a81de4b0e8cd817e74c81589b7c0543e4d2a12918782b6a0b62fbb2e9";


    public Jws<Claims> vlidateToken(String token) {
        try{
            Jws<Claims> claimsJws= Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return claimsJws;
        }catch(Exception e){
         System.out.println("Exception is here "+e.getMessage());
         throw new RuntimeException(e.getMessage());
        }

    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims,username);
    }

    public String createToken(Map<String, Object> claims, String userName) {
        System.out.println("inside the create service method");
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
