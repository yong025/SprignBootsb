package org.zerock.sb.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.sb.security.filter.TokenCheckFilter;
import org.zerock.sb.security.filter.TokenGenerateFilter;
import org.zerock.sb.security.util.JWTUtil;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true) //권한을 제어할 수 있게 한다.
@RequiredArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("CustomSecurityConfig........configure..........");
        log.info("CustomSecurityConfig........configure..........");
        log.info("CustomSecurityConfig........configure..........");
        log.info("CustomSecurityConfig........configure..........");

        http.formLogin().loginPage("/customLogin").loginProcessingUrl("/login"); //인가/인증에 문제시 로그인 화면
        http.csrf().disable();
        http.logout();

        http.addFilterBefore(tokenCheckFilter(), UsernamePasswordAuthenticationFilter.class); //일반적인 로그인하기 전에 얘를 먼저 동작시킨다.
        http.addFilterBefore(tokenGenerateFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TokenCheckFilter tokenCheckFilter(){//언제 토큰을 체크할것인지 - 사용자가 접근할때
        return new TokenCheckFilter(jwtUtil());
    }

    @Bean
    public JWTUtil jwtUtil(){ //Token을 체크할 때 필요하다.
        return new JWTUtil();
    }

    @Bean
    public TokenGenerateFilter tokenGenerateFilter() throws Exception{

        return new TokenGenerateFilter("/jsonApiLogin", authenticationManager(), jwtUtil());
        //jsonApiLogin으로 오는데 인증매니저를 통해 온다.
    }
}
