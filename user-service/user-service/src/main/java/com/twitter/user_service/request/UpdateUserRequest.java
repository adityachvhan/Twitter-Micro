package com.twitter.user_service.request;

import lombok.Data;

@Data
public class UpdateUserRequest {

	private String fullName;

	private String image;

	private String backgroundImage;

	private String bio;

	private String location;

	private String website;

	private String birthDate;

	private String mobile;
}
