package com.twitter.authservice.service;

import com.twitter.authservice.model.AuthCredential;
import com.twitter.authservice.repository.AuthCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthCredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthCredential credential = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No credentials found for: " + email));

        if (credential.isLoginWithGoogle()) {
            throw new UsernameNotFoundException("This account uses Google login — password signin not allowed.");
        }

        return new User(credential.getEmail(), credential.getPassword(), new ArrayList<>());
    }
}
