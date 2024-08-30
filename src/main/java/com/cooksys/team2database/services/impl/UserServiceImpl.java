package com.cooksys.team2database.services.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserRequestDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.Credentials;
import com.cooksys.team2database.entities.Profile;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.BadRequestException;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.CredentialsMapper;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.UserService;

import lombok.RequiredArgsConstructor;

// TODO: make error checks and exceptions for the get endpoints if tests fail
// TODO: possibly clean up code

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final CredentialsMapper credentialsMapper;
	private final UserRepository userRepository;

	// check if credentials is null
	private void validateCredentials(Credentials credentials) {
		if (credentials == null || credentials.getPassword() == null || credentials.getUsername() == null) {
			throw new BadRequestException("Credentials must not be null!");
		}
	}

	// check if profile is null
	private void validateProfile(Profile profile) {
		if (profile == null || profile.getEmail() == null) {
			throw new BadRequestException("Profile must not be null!");
		}
	}

	// checks if user exists in database
	private void doesUserExist(Optional<User> user) {
		if (user.isEmpty() || user.get().isDeleted()) {
			throw new NotFoundException("User not found!");
		}
	}

	private void validateCreateUser(UserRequestDto userRequestDto) {

		validateCredentials(userMapper.requestDtoToEntity(userRequestDto).getCredentials());
		validateProfile(userMapper.requestDtoToEntity(userRequestDto).getProfile());

		// if the given username exists, throw error.
		// Originally, I tried to use NotUnauthorizedException but it throws an error
		// 500 (fails the postman test).
		// BadRequestException throws a 400 (passes the postman test)
		if (userRepository.existsByCredentialsUsername(
				userMapper.requestDtoToEntity(userRequestDto).getCredentials().getUsername())) {
			throw new BadRequestException("Username already exists!");
		}

	}

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

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		validateCreateUser(userRequestDto);
		User newUser = new User();
		newUser.setCredentials(userMapper.requestDtoToEntity(userRequestDto).getCredentials());
		newUser.setProfile(userMapper.requestDtoToEntity(userRequestDto).getProfile());
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(newUser));
	}

	// this can definitely be cleaned up
	// possible redundancy in the error checks?
	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		validateCredentials(credentialsMapper.dtoToEntity(credentialsDto));

		Optional<User> follower = userRepository
				.findByCredentialsUsername(credentialsMapper.dtoToEntity(credentialsDto).getUsername());
		doesUserExist(follower);

		Optional<User> followed = userRepository.findByCredentialsUsername(username);
		doesUserExist(followed);

		// if the following relationship already exists
		if (follower.get().getFollowing().contains(followed.get())
				&& followed.get().getFollowers().contains(follower.get())) {
			throw new BadRequestException(follower.get().getCredentials().getUsername() + " is already following "
					+ followed.get().getCredentials().getUsername());
		}

		// add the following relationship
		List<User> followersList = follower.get().getFollowing();
		followersList.add(followed.get());
		follower.get().setFollowing(followersList);
		userRepository.saveAndFlush(follower.get());

		List<User> followingList = followed.get().getFollowers();
		followingList.add(follower.get());
		followed.get().setFollowers(followingList);
		userRepository.saveAndFlush(followed.get());

	}

	// possible redundancy in the error checks?
	// TODO: Cannot determine if deletion is successful until other methods are
	// implemented
	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		validateCredentials(credentialsMapper.dtoToEntity(credentialsDto));

		Optional<User> userToDelete = userRepository
				.findByCredentialsUsername(credentialsMapper.dtoToEntity(credentialsDto).getUsername());
		doesUserExist(userToDelete);

		// check if the provided credentials match the user
		if (!userToDelete.get().getCredentials().getUsername().equals(username)) {
			throw new BadRequestException("Username credentials mismatch!");
		}

		// delete all tweets from the user
		for (Tweet tweet : userToDelete.get().getTweets()) {
			tweet.setDeleted(true);
		}

		// delete user
		userToDelete.get().setDeleted(true);

		return userMapper.entityToResponseDto(userRepository.saveAndFlush(userToDelete.get()));
	}

}
