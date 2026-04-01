package com.twitter.authservice;

import com.twitter.authservice.config.JwtTokenProvider;
import com.twitter.authservice.dto.AuthDtos;
import com.twitter.authservice.exception.AuthException;
import com.twitter.authservice.model.AuthCredential;
import com.twitter.authservice.repository.AuthCredentialRepository;
import com.twitter.authservice.service.AuthService;
import com.twitter.authservice.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock AuthCredentialRepository credentialRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock CustomUserDetailsService userDetailsService;
    @Mock RestTemplate restTemplate;

    @InjectMocks AuthService authService;

    @BeforeEach
    void injectUrl() throws Exception {
        var field = AuthService.class.getDeclaredField("userServiceUrl");
        field.setAccessible(true);
        field.set(authService, "http://localhost:8082");
    }

    @Test
    void signup_succeeds_forNewEmail() throws AuthException {
        when(credentialRepository.findByEmail("alice@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(jwtTokenProvider.generateToken("alice@test.com")).thenReturn("jwt-token");

        AuthDtos.SignupRequest req = new AuthDtos.SignupRequest();
        req.setEmail("alice@test.com");
        req.setPassword("secret");
        req.setFullName("Alice");

        AuthDtos.AuthResponse res = authService.signup(req);

        assertThat(res.getJwt()).isEqualTo("jwt-token");
        assertThat(res.isStatus()).isTrue();
        verify(credentialRepository).save(any(AuthCredential.class));
    }

    @Test
    void signup_throws_forDuplicateEmail() {
        AuthCredential existing = new AuthCredential();
        existing.setEmail("alice@test.com");
        when(credentialRepository.findByEmail("alice@test.com")).thenReturn(Optional.of(existing));

        AuthDtos.SignupRequest req = new AuthDtos.SignupRequest();
        req.setEmail("alice@test.com");
        req.setPassword("secret");
        req.setFullName("Alice");

        assertThatThrownBy(() -> authService.signup(req))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("already registered");
    }

    @Test
    void signin_succeeds_withValidCredentials() {
        User mockUser = new User("alice@test.com", "hashed", new ArrayList<>());
        when(userDetailsService.loadUserByUsername("alice@test.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("secret", "hashed")).thenReturn(true);
        when(jwtTokenProvider.generateToken("alice@test.com")).thenReturn("jwt-token");

        AuthDtos.SigninRequest req = new AuthDtos.SigninRequest();
        req.setEmail("alice@test.com");
        req.setPassword("secret");

        AuthDtos.AuthResponse res = authService.signin(req);
        assertThat(res.getJwt()).isEqualTo("jwt-token");
    }

    @Test
    void signin_throws_withBadPassword() {
        User mockUser = new User("alice@test.com", "hashed", new ArrayList<>());
        when(userDetailsService.loadUserByUsername("alice@test.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        AuthDtos.SigninRequest req = new AuthDtos.SigninRequest();
        req.setEmail("alice@test.com");
        req.setPassword("wrong");

        assertThatThrownBy(() -> authService.signin(req))
                .isInstanceOf(BadCredentialsException.class);
    }
}
