package org.zerock.sb.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.security.util.JWTUtil;

@SpringBootTest
@Log4j2
public class JWTTests {

    @Autowired
    JWTUtil jwtUtil;

    @Test
    public void testGenerate(){

        String jwtStr = jwtUtil.generateToken("user11");

        log.info(jwtStr);
    }

    @Test
    public void testValidate(){

        String str = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTEiLCJpYXQiOjE2MzQ4Njg5NTUsImV4cCI6MTYzNDg3MjU1NX0.5LJmxxqsXa21W3_pKlAJuCUik2ytTyuekB-DU0egvQA";

        try {
            jwtUtil.validateToken(str);
        }catch (ExpiredJwtException ex){//상위 에러 - 유효기간이 지난 토큰
            log.error("expired.................");

            log.error(ex.getMessage());

        }catch (JwtException ex){//유효기간 외의 에러
            log.error("jwtException......................");
            log.error(ex.getMessage());
        }
    }

}
