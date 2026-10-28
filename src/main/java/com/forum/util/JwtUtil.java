package com.forum.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // 签名密钥：绝对不能泄露，生产环境放配置里
    private static final String SECRET = "forum-jwt-secret-key-must-be-256-bits-long!!";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 小时

    /** 生成令牌 */
    public static String generateToken(Long userId, String username) {
        return Jwts.builder()
                .subject(String.valueOf(userId))   // 主题：存用户 ID
                .claim("username", username)        // 自定义字段：存用户名
                .issuedAt(new Date())               // 签发时间
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 过期时间
                .signWith(KEY)                      // 签名
                .compact();                         // 打包成字符串
    }

    /** 解析令牌 */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}