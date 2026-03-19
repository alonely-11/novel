package com.novel.core.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@ConditionalOnProperty("novel.jwt.secret")
@Slf4j
public class JwtUtils {

    /**
     * 注入jwt加密密钥
     */
    @Value("${novel.jwt.secret}")
    public String secret;

    /**
     * 定义系统标识头常量
     */
    public static final String HEADER_SYSTEM_KEY = "systemKeyHeader";

    /**
     * 根据用户id生成jwt
     * @param uid 用户id
     * @param systemKey 系统标识
     * @return JWT
     */
    public String generateToken(Long uid,String systemKey){
        return Jwts.builder()
                .setHeaderParam(HEADER_SYSTEM_KEY,systemKey)
                .setSubject(uid.toString())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Long parseToken(String token,String systemKey) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            if (Objects.equals(claimsJws.getHeader().get(HEADER_SYSTEM_KEY), systemKey)) {
                return Long.parseLong(claimsJws.getBody().getSubject());
            }
        } catch (JwtException e) {
            log.warn("JWT解析失败：{}",token);
        }
        return null;
    }
}
