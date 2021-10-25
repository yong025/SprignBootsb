package org.zerock.sb.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private final static String secretKey = "helloworld111112222233333333344444444444555555555";

    private SecretKey key; //암호화하는 라이브러리

    public JWTUtil() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String content) {

        long timeAmount = 1; //유효기간 설정해주기(분 단위)

        String jws = Jwts.builder() // (1)
        .setSubject(content)      // (2) //id를 검사한다.
        .setIssuedAt(new Date())
        .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(timeAmount).toInstant()))
        .signWith(key, SignatureAlgorithm.HS256)          // (3)
        .compact();             // (4) //문자열을 만들어 낸다.

        return jws;
    }

    public void validateToken(String token) throws JwtException {

        Jws<Claims> jws = Jwts.parserBuilder()  // (1) //토큰의 만료시간이 끝났을 수 있고, 다를 수 있는 문제발생
                .setSigningKey(key)         // (2)
                .build()                    // (3)
                .parseClaimsJws(token); // (4)

    }
}
