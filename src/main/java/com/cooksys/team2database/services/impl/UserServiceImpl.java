package com.cooksys.team2database.services.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entityToResponseDtos(userRepository.findAll());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		return userMapper.entityToResponseDto(getUser(username));
	}
	
	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User user = getUser(username);
		List<Tweet> userTweets = user.getTweets();
		sortTweets(userTweets);
		
		return tweetMapper.entitiesToDtos(userTweets);
	}
	
	@Override
	public List<TweetResponseDto> getUserFeed(String username) {
		User user = getUser(username);
		List<Tweet> userFeed = user.getTweets();
		for (User u : user.getFollowing()) {
			userFeed.addAll(0, u.getTweets());
		}
		sortTweets(userFeed);
		
		return tweetMapper.entitiesToDtos(userFeed);
	}
	
	@Override
	public List<UserResponseDto> getUserFollowers(String username) {
		User user = getUser(username);
		
		return userMapper.entityToResponseDtos(user.getFollowers());
	}
	
	@Override
	public List<UserResponseDto> getUserFollowing(String username) {
		User user = getUser(username);
		
		return userMapper.entityToResponseDtos(user.getFollowing());
	}
	
	private User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().isDeleted()) {
			throw new NotFoundException("No user found with username: @" + username);
		}
		
		return optionalUser.get();
	}
	
	private void sortTweets(List<Tweet> tweets) {
		Collections.sort(tweets, Comparator.comparing(Tweet::getPosted).reversed());
	}

}
