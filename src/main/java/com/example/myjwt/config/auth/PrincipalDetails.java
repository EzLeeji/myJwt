package com.example.myjwt.config.auth;

import com.example.myjwt.model.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data //테스트 후 지우기
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }

    //유저의 권한을 return 하는 부분.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        user.getRoleList().forEach(s -> collection.add(() -> s));
        return collection;
    }

    //유저의 패스워드를 return
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //유저 이름(id) return
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료 체크 => 이 계정이 만료가 안됐습니까?  네! (true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠금 체크 => 이 계정이 잠금이 안됐습니까?  네! (true)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //날짜에 관련해서 계정이 어떤 부분에 대해서 너무 오래되지 않았죠? 네! (true)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //이 계정은 활성화 되어 있습니까? 네! (true)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
