package com.apiGateWay.apiGateWay.utill;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtUtill {
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

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
