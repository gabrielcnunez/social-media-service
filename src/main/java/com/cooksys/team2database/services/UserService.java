package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserRequestDto;
import com.cooksys.team2database.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	List<TweetResponseDto> getUserTweets(String username);
	
	List<TweetResponseDto> getUserFeed(String username);

	List<UserResponseDto> getUserFollowers(String username);

	List<UserResponseDto> getUserFollowing(String username);
	
	// TODO: error check for  no credentials and no profile
	UserResponseDto updateUser(String username, UserRequestDto userRequestDto);
	
	// TODO: user 2 unfollows first user then refollows (fails)
    void unfollowUser(String usernameToUnfollow, CredentialsDto credentials);

	UserResponseDto createUser(UserRequestDto userRequestDto);

	void followUser(String username, CredentialsDto credentialsDto);

	// TODO: retest when user is deleted then re-added with same credentials (flip boolean isDeleted())
	UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);

}
