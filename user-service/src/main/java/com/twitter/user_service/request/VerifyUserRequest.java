package com.twitter.user_service.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VerifyUserRequest {

	// "monthly" or "yearly"
	private String planType;

	private LocalDateTime startedAt;

	private LocalDateTime endsAt;
}
