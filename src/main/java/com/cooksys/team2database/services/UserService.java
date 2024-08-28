package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	List<TweetResponseDto> getUserTweets(String username);

	List<UserResponseDto> getUserFollowers(String username);

}
