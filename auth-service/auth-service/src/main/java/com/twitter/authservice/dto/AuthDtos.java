package com.twitter.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class SignupRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String fullName;
        private String birthDate;
    }

    @Data
    public static class SigninRequest {
        @NotBlank @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String jwt;
        private boolean status;
        private String message;

        public AuthResponse(String jwt, boolean status, String message) {
            this.jwt = jwt;
            this.status = status;
            this.message = message;
        }
    }

    /** Payload sent to user-service /internal/users after signup */
    @Data
    public static class CreateUserProfileRequest {
        private String email;
        private String fullName;
        private String birthDate;
        private boolean loginWithGoogle;
    }
}
