package com.ae.community.config.security;

import com.ae.community.domain.CommunityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private Long tokenValidMillisecond = 60 * 60 * 1000L;

    //private final CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 생성
    public String createToken(CommunityUser user) {
        // 기한은 지금부터 90일로 설정
        Date expiryDate = Date.from(Instant.now().plus(90, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setSubject(user.getUserIdx().toString())
                .setIssuer("app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    // Token 내용을 뜯어서 id 얻기
    public String validateAndGetUserId(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("*** token 내용에 에러가 있음 -- JwtProvide");
            return "INVALID JWT";
        }
    }
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
