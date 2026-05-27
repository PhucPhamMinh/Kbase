package com.kbase.user.controller;

import com.kbase.user.common.ResponseTemplate;
import com.kbase.user.dto.UpdateProfileRequest;
import com.kbase.user.dto.UserResponse;
import com.kbase.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseTemplate<List<UserResponse>> findAll() {
        return ResponseTemplate.success("Users loaded", userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseTemplate<UserResponse> findById(@PathVariable Long userId) {
        return ResponseTemplate.success("User loaded", userService.findById(userId));
    }

    @GetMapping("/me")
    public ResponseTemplate<UserResponse> me(@RequestHeader("X-User-Id") Long userId) {
        return ResponseTemplate.success("Profile loaded", userService.findById(userId));
    }

    @PutMapping("/me")
    public ResponseTemplate<UserResponse> updateMe(@RequestHeader("X-User-Id") Long userId,
                                                   @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseTemplate.success("Profile updated", userService.updateProfile(userId, request));
    }
}
