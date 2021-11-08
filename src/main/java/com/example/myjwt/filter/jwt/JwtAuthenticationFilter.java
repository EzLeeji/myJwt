package com.example.myjwt.filter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.myjwt.config.auth.PrincipalDetails;
import com.example.myjwt.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티 필터체인에 UsernamePasswordAuthenticationFilter가 존재
// login 요청이 들어오면 (POST)
// UsernamePasswordAuthenticationFilter가 동작을 함.
// formLogin().disable()처리했기 때문에 UsernamePasswordAuthenticationFilter를 시큐리티 필터에 직접 넣어줌.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //login 요청 시 동작함.
    // attemptAuthentication 함수가 종료되면 successfulAuthentication이 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        //1. id, pw 받아서 회원 여부 확인

        //2. authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출이 된다.

        //3. PrinciaplDetails를 세션에 담은 후 ( JWT인데 왜 세션? 시큐리티에게 권한을 위임하기 위해 세션에 넣음 권한이 없으면 세션필요없음)

        //4. JWT 토큰을 만들어서 응답해주면 된다.
        System.out.println("attemptAuthentication : 로그인 시도중");
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            //이 때 PrincipalDetailsService의 loadUserByUsername 실행됨
            // authentication에는 유저 정보가 들어감.
            Authentication authentication = authenticationManager.authenticate(token);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 정상이면 유저값이 나옴 : " + principalDetails.getUser().getUsername());

            // 이 객체는 세션영역에 들어감.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 인증이 정상적으로 처리되면 successfulAuthentication이 실행
    // 여기서 토큰을 만들어서 사용자에게 response해도된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 인증이 완료됨.");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("JWT TOKEN")
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id",principalDetails.getUser().getId())//withClaim 넣고 싶은 키:벨류 값
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX +jwtToken);
    }

}
