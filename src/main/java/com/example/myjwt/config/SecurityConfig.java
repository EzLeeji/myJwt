package com.example.myjwt.config;

import com.example.myjwt.filter.jwt.JwtAuthenticationFilter;
import com.example.myjwt.filter.jwt.JwtAuthorizationFilter;
import com.example.myjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        //http.addFilterBefore(new SbeforeFilter(), SecurityContextPersistenceFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠습니다.
            .and()
            .addFilter(corsFilter)
            .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            .and()
            .formLogin().disable() //jwt 서버라서 폼로그인 사용 안함
            .httpBasic().disable() //기존 방식은 헤더에 id, pw를 달고 인증을 함. 헤더에 token을 넣어서 처리하기 위해 기존방식은 disable처리함.
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))//formLogin을 disable했기때문에 직접 필터를 넣어줌.
            .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))// 토큰 확인용 필터.
            .authorizeRequests()
            .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll();
    }

}
