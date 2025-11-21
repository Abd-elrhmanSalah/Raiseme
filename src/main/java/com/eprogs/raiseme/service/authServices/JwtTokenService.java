package com.eprogs.raiseme.service.authServices;


import com.eprogs.raiseme.dto.authDTOS.TokenGenerationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;

import static com.eprogs.raiseme.constant.ApplicationConstant.JWT_SECRET_KEY;
import static com.eprogs.raiseme.constant.ApplicationConstant.JWT_TOKEN_VALIDITY_IN_MILLIS;

@Service
public class JwtTokenService implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public String generateToken(TokenGenerationDTO tokenGenerationDTO) {
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject("JWT token")
                .claim("username", tokenGenerationDTO.getUserDetails().getUsername())
                .claim("firstName", tokenGenerationDTO.getUserDetails().getFirstName())
                .claim("lastName", tokenGenerationDTO.getUserDetails().getLastName())
                .claim("isActive", tokenGenerationDTO.getUserDetails().isActive());

        if (tokenGenerationDTO.getUserId() != null) {
            jwtBuilder.claim("userId", tokenGenerationDTO.getUserId());
        }

        return jwtBuilder
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_IN_MILLIS))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = getAllClaimsFromToken(token);
        final String username = String.valueOf(claims.get("username"));
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY.getBytes()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY.getBytes()).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}
