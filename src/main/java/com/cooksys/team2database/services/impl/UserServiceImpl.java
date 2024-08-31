package com.cooksys.team2database.services.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.ProfileDto;
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
	private boolean doesUserExist(Optional<User> user) {
		if (user.isEmpty() || user.get().isDeleted()) {
			return false;
		}
		return true;
	}

	private boolean validateCreateUserRequest(UserRequestDto userRequestDto) {

		validateCredentials(userMapper.requestDtoToEntity(userRequestDto).getCredentials());
		validateProfile(userMapper.requestDtoToEntity(userRequestDto).getProfile());

		Optional<User> user = userRepository.findByCredentialsUsername(
				userMapper.requestDtoToEntity(userRequestDto).getCredentials().getUsername());

		// check if the username already exists
		if (user.isEmpty()) {
			return true;
		}

		Credentials requestedCredentials = userMapper.requestDtoToEntity(userRequestDto).getCredentials();
		Credentials existingCredentials = user.get().getCredentials();

		// if the username already exists, check if the requested credentials match the
		// existing credentials
		if (existingCredentials.getUsername().equals(requestedCredentials.getUsername())) {

			// if the user is not deleted
			if (!user.get().isDeleted()) {
				throw new BadRequestException("User already exists!");
			}

			// if the requested password is incorrect
			// TODO: change to unauthorized error later
			if (!existingCredentials.getPassword().equals(requestedCredentials.getPassword())) {
				throw new BadRequestException("Incorrect password!");
			}
		}
		return false;
	}

	private UserResponseDto reactivateUser(UserRequestDto userRequestDto) {
		User updatedUser = userMapper.requestDtoToEntity(userRequestDto);
		Optional<User> reactivatedUser = userRepository.findByCredentialsUsername(
				userMapper.requestDtoToEntity(userRequestDto).getCredentials().getUsername());
		// re-activate all tweets
		for (Tweet tweet : reactivatedUser.get().getTweets()) {
			tweet.setDeleted(false);
		}
		// re-activate user
		reactivatedUser.get().setDeleted(false);
		reactivatedUser.get().setProfile(updatedUser.getProfile());
		return userMapper.entityToResponseDto(userRepository.saveAndFlush(reactivatedUser.get()));
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
	public List<TweetResponseDto> getUserMentions(String username) {
		User user = getUser(username);
		List<Tweet> userMentions = user.getMentionedTweets();
		sortTweets(userMentions);

		return tweetMapper.entitiesToDtos(userMentions);
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
		if (validateCreateUserRequest(userRequestDto)) {
			User newUser = new User();
			newUser.setCredentials(userMapper.requestDtoToEntity(userRequestDto).getCredentials());
			newUser.setProfile(userMapper.requestDtoToEntity(userRequestDto).getProfile());
			return userMapper.entityToResponseDto(userRepository.saveAndFlush(newUser));
		}
		return reactivateUser(userRequestDto);
	}

	// this can definitely be cleaned up
	// possible redundancy in the error checks?

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
		validateCredentials(credentialsMapper.dtoToEntity(credentialsDto));

		Optional<User> follower = userRepository
				.findByCredentialsUsername(credentialsMapper.dtoToEntity(credentialsDto).getUsername());
		if (!doesUserExist(follower)) {
			throw new NotFoundException("User not found!");
		}

		Optional<User> followed = userRepository.findByCredentialsUsername(username);
		if (!doesUserExist(followed)) {
			throw new NotFoundException("User not found!");
		}

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

	@Override
	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		// Retrieve the user by username
		User user = getUser(username);

		// Validate credentials
		if (userRequestDto.getCredentials() == null) {
			throw new BadRequestException("Credentials must be provided");
		}

		// Verify the provided credentials match the user's current credentials
		if (!user.getCredentials().getUsername().equals(userRequestDto.getCredentials().getUsername())
				|| !user.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
			throw new BadRequestException("Invalid credentials");
		}

		// Check if profile update is requested
		if (userRequestDto.getProfile() != null) {
			Profile userProfile = user.getProfile();
			ProfileDto newProfileDto = userRequestDto.getProfile();

			// Update profile fields if new values are provided
			if (newProfileDto.getFirstName() != null) {
				userProfile.setFirstName(newProfileDto.getFirstName());
			}
			if (newProfileDto.getLastName() != null) {
				userProfile.setLastName(newProfileDto.getLastName());
			}
			if (newProfileDto.getEmail() != null) {
				userProfile.setEmail(newProfileDto.getEmail());
			}
			if (newProfileDto.getPhone() != null) {
				userProfile.setPhone(newProfileDto.getPhone());
			}
		} else {
			throw new BadRequestException("Profile must be provided for update");
		}

		// Save the updated user to the database and flush changes
		User updatedUser = userRepository.saveAndFlush(user);

		// Convert the updated user entity to a DTO and return it
		return userMapper.entityToResponseDto(updatedUser);
	}

	@Override
	public void unfollowUser(String usernameToUnfollow, CredentialsDto credentials) {
		// Retrieve the user to unfollow
		User userToUnfollow = getUser(usernameToUnfollow);

		// Find the follower user by their username
		User follower = userRepository.findByCredentialsUsername(credentials.getUsername())
				.orElseThrow(() -> new BadRequestException("Invalid credentials"));

		// Verify the follower's password
		if (!follower.getCredentials().getPassword().equals(credentials.getPassword())) {
			throw new BadRequestException("Invalid credentials");
		}

		// Check if the follower is actually following the user
		if (!follower.getFollowing().contains(userToUnfollow)) {
			throw new BadRequestException("Not following this user");
		}

		// Remove the userToUnfollow from the follower's following list and remove the
		// follower from the userToUnfollow's followers list
		follower.getFollowing().remove(userToUnfollow);
		userToUnfollow.getFollowers().remove(follower);

		// Save both updated users in a single transaction
		userRepository.saveAll(Arrays.asList(follower, userToUnfollow));
	}

}
