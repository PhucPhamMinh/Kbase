package com.kbase.user.controller;

import com.kbase.user.common.ResponseTemplate;
import com.kbase.user.dto.AuthResponse;
import com.kbase.user.dto.LoginRequest;
import com.kbase.user.dto.RegisterRequest;
import com.kbase.user.dto.UserResponse;
import com.kbase.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseTemplate<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseTemplate.success("User registered successfully", userService.register(request));
    }

    @PostMapping("/login")
    public ResponseTemplate<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseTemplate.success("Login successful", userService.login(request));
    }
}
