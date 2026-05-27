package com.kbase.user.service;

import com.kbase.user.dto.AuthResponse;
import com.kbase.user.dto.LoginRequest;
import com.kbase.user.dto.RegisterRequest;
import com.kbase.user.dto.UpdateProfileRequest;
import com.kbase.user.dto.UserResponse;
import com.kbase.user.exception.BadRequestException;
import com.kbase.user.exception.ResourceNotFoundException;
import com.kbase.user.model.User;
import com.kbase.user.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByEmail(request.email())) {
                throw new BadRequestException("Email is already registered");
            }
            User user = new User();
            user.setEmail(request.email().toLowerCase());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setFullName(request.fullName());
            user.setRole(request.role());
            return toResponse(userRepository.save(user));
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Unable to register user");
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.email().toLowerCase())
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));
            if (!Boolean.TRUE.equals(user.getActive()) || !passwordEncoder.matches(request.password(), user.getPassword())) {
                throw new BadRequestException("Invalid credentials");
            }
            return new AuthResponse(jwtService.createToken(user), toResponse(user));
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException("Unable to authenticate user");
        }
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse findById(Long userId) {
        return toResponse(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(request.fullName());
        return toResponse(userRepository.save(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getUserId(), user.getEmail(), user.getFullName(), user.getRole(), user.getActive());
    }
}
