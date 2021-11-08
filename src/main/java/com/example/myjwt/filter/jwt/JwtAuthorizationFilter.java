package com.example.myjwt.filter.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.myjwt.config.auth.PrincipalDetails;
import com.example.myjwt.model.entity.User;
import com.example.myjwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 가지고 있는 필터중에 BasicAuthenticationFilter 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 탄다.
//
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("인증이나 권한이 필요한 주소 요청왔음 헤더 값 : "+jwtHeader);

        //header 확인
        if(jwtHeader==null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        //JWT 검증하기
        String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX,"");
        String username = JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build()
                .verify(jwtToken)
                .getClaim("username")
                .asString();

        //username이 존재, 정상적
        if(username!=null){
            User user = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(user);

            //로그인 시도가 아니니깐 null을 넣는다.
            //jwt 토큰 서명을 통해서 서명이 정상이면  authentication 객체를 만든다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails,null, principalDetails.getAuthorities());

            //SecurityContextHolder 시큐리티 세션 공간에 접근해서 Authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }

    }


}
