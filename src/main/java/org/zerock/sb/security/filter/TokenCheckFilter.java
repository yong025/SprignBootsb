package org.zerock.sb.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.sb.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class TokenCheckFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public TokenCheckFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("-------------TokenCheckFilter------------------------"); // 어디에 끼워넣을지가 중요함

        String path = request.getRequestURI();

        log.info(path);

        if(path.startsWith("/api/")){ //   /api로 들어온다면 checktoken
            //checkToken
            String authToken = request.getHeader("Authorization");//<< key(name)이  없으면 토큰이 없는애다.

            if(authToken == null){ //authToken이 없다면
                log.info("authToken is null...............................");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject(); //json데이터를 새로 생성한다.
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }

            //jwt 검사 맨앞에 인증 타입:Bearer 토큰
            String tokenStr = authToken.substring(7); //auth토큰을 보내는게 아니라 tokenStr만 보내서 검사를 해야한다.

            try {
                jwtUtil.validateToken(tokenStr);
            }catch (ExpiredJwtException ex){//오래된 토큰

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "EXPIRED API TOKEN.. TOO OLD";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }catch (JwtException jex){//토큰이 다를때

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                // json 리턴
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "YOUR ACCESS TOKEN IS INVALID!";
                json.put("code", "401");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                out.close();
                return;
            }

            filterChain.doFilter(request, response); //api는 기본적으로 무상태

        }else {
            log.info("=============TokenCheckFilter=========================");
            //다음단계로 진행시키는 기능
            filterChain.doFilter(request, response);
        }

    }
}
