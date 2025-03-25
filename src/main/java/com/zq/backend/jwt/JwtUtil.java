package com.zq.backend.jwt;

import com.zq.backend.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createToken(String subject, int jwtVersion, Date now, long ttlMillis) {
        SecretKey secretKey = generalKey();

        long expMillis = now.getTime() + ttlMillis;
        Date expDate = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .claim(Constant.JWT_VERSION_KEY, jwtVersion)
                .id(getUUID())
                .subject(subject)
                .issuedAt(now)
                .signWith(secretKey)
                .expiration(expDate);
        return builder.compact();
    }

    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(Constant.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, Constant.JWT_ALGORITHM);
    }

    public static Claims parseToken(String token) {
        try {
            SecretKey secretKey = generalKey();
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if(claims.getExpiration().before(new Date())) {
                return null;
            }
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
