package com.example.myjwt.service;

import com.example.myjwt.model.entity.User;
import com.example.myjwt.model.request.UserRequest;
import com.example.myjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void addUser(UserRequest userRequest){
        String encPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());
        User user = User.builder()
            .username(userRequest.getUsername())
            .password(encPassword)
            .roles(userRequest.getRoles())
            .build();
        userRepository.save(user);
    }

}
