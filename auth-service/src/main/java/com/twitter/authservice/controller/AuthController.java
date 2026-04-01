package com.twitter.authservice.controller;

import com.twitter.authservice.dto.AuthDtos;
import com.twitter.authservice.exception.AuthException;
import com.twitter.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Signup, signin, JWT issuance")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user account")
    @PostMapping("/signup")
    public ResponseEntity<AuthDtos.AuthResponse> signup(
            @Valid @RequestBody AuthDtos.SignupRequest req) throws AuthException {
        AuthDtos.AuthResponse response = authService.signup(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Sign in and receive a JWT")
    @PostMapping("/signin")
    public ResponseEntity<AuthDtos.AuthResponse> signin(
            @Valid @RequestBody AuthDtos.SigninRequest req) {
        AuthDtos.AuthResponse response = authService.signin(req);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("auth-service is running");
    }
}
