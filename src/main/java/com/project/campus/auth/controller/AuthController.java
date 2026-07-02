package com.project.campus.auth.controller;

import com.project.campus.auth.dto.AuthRequest;
import com.project.campus.auth.dto.AuthResponse;
import com.project.campus.auth.service.JwtService;
import com.project.campus.user.model.Role;
import com.project.campus.user.model.User;
import com.project.campus.user.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
