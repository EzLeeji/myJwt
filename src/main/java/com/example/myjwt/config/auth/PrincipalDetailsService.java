package com.example.myjwt.config.auth;

import com.example.myjwt.model.entity.User;
import com.example.myjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//loginProcessingUrl("/loginProc") 이 요청이 오면
// UserDetailsService의 loadUserByUsername가 실행이 된다.
// 하지만 시큐리티 콘피그에서 formLogin을 disable 했으니 이런식으로 동작하지 않음.
// 동작을 위해서 JwtAuthenticationFilter에서 authenticationManager로 로그인을 시도해주면 동작함.

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user!=null){
            return new PrincipalDetails(user); //return을 하게 되면 시큐리티 세션에 Authentication[UserDetails(User)] 타입으로 들어가 있다.
        }
        return null;
    }
}
