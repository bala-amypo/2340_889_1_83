package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public User register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String role) {
        return userService.register(email, password, role);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password) {

        User user = userService.getByEmail(email);
        return jwtTokenProvider.createToken(
                user.getEmail(),
                user.getRole(),
                user.getId()
        );
    }
}
