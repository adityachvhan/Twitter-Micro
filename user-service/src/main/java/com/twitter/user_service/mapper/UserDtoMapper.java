package com.twitter.user_service.mapper;

import java.util.ArrayList;
import java.util.List;

import com.twitter.user_service.dto.UserDto;
import com.twitter.user_service.model.User;

public class UserDtoMapper {

	public static UserDto toUserDto(User user) {

		UserDto userDto = new UserDto();

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setFullName(user.getFullName());
		userDto.setImage(user.getImage());
		userDto.setBackgroundImage(user.getBackgroundImage());
		userDto.setBio(user.getBio());
		userDto.setLocation(user.getLocation());
		userDto.setWebsite(user.getWebsite());
		userDto.setBirthDate(user.getBirthDate());
		userDto.setMobile(user.getMobile());
		userDto.setLoginWithGoogle(user.isLoginWithGoogle());

		userDto.setFollowers(toSlimUserDtos(user.getFollowers()));
		userDto.setFollowing(toSlimUserDtos(user.getFollowings()));

		return userDto;
	}

	private static List<UserDto> toSlimUserDtos(List<User> followers) {

		List<UserDto> userDtos = new ArrayList<UserDto>();

		for (User user : followers) {

			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setEmail(user.getEmail());
			userDto.setFullName(user.getFullName());
			userDto.setImage(user.getImage());

			userDtos.add(userDto);
		}

		return userDtos;
	}
}
