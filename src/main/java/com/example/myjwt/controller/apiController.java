package com.example.myjwt.controller;

import com.example.myjwt.model.request.UserRequest;
import com.example.myjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class apiController {

    private final UserService userService;

    @GetMapping("/home")
    public String home(){
        return "어서오세요.";
    }

    @PostMapping("/user")
    public String addUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return "가입완료";
    }

    @GetMapping("/api/v1/user")
    public String user(){
        return "user,manager,admin권한!";
    }

    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager,admin권한!";
    }

    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin권한";
    }


}
