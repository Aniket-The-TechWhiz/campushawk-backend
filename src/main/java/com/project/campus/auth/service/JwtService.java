//package com.project.campus.auth.service;
//
//import com.project.campus.user.model.User;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//@Service
//public class JwtService {
//    private static final String secret="thisisaverylongsecretkeyfortestingjwtsecurityapplication";
//
//    private Key getSignkey(){
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public String generateToken(User user){
//        return Jwts.builder()
//                .subject(user.getEmail())
//                .claim("role",user.getRole().name())
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis()+3600000))
//                .signWith(getSignkey())
//                .compact();
//    }
//
//    public String extractUsername(String token){
//        return Jwts.parser()
//                .verifyWith((SecretKey) getSignkey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//}
