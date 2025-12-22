package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {
    User register(String email, String password, String role);
    User login(String email, String password);
    User getByEmail(String email);
}