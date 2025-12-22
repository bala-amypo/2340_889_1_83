package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // POST /auth/register
    @PostMapping("/register")
    public User register(@RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String role) {
        return userService.register(email, password, role);
    }

    // POST /auth/login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String email,
                                     @RequestParam String password) {

        User user = userService.login(email, password);
        String token = jwtTokenProvider.createToken(
                user.getEmail(), user.getRole(), user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }
}
