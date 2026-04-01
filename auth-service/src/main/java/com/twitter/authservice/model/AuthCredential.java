package com.twitter.authservice.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Auth-service only stores credentials — email and the BCrypt-hashed password.
 * All profile data (name, image, bio …) lives in user-service / user_db.
 */
@Entity
@Table(name = "auth_credentials")
@Data
public class AuthCredential {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	private boolean loginWithGoogle = false;
}
