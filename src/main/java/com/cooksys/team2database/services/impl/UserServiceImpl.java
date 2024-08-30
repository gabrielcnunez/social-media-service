package com.cooksys.team2database.services.impl;

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
import com.cooksys.team2database.entities.Profile;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.BadRequestException;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.UserService;

import jakarta.transaction.Transactional;
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

    @Transactional
    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        // Retrieve the user by username
        User user = getUser(username);

        // Verify the provided credentials match the user's current credentials
        if (!user.getCredentials().getUsername().equals(userRequestDto.getCredentials().getUsername()) ||
            !user.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
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
        }

        // Save the updated user to the database and flush changes
        User updatedUser = userRepository.saveAndFlush(user);

        // Convert the updated user entity to a DTO and return it
        return userMapper.entityToResponseDto(updatedUser);
    }
    
    @Transactional
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

        // Remove the userToUnfollow from the follower's following list and remove the follower from the userToUnfollow's followers list
        follower.getFollowing().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(follower);

        // Save the updated  follow and unfollow database
        userRepository.save(follower);
        userRepository.save(userToUnfollow);
    }

    private User getUser(String username) {
        return userRepository.findByCredentialsUsername(username)
            .orElseThrow(() -> new NotFoundException("No user found with username: @" + username));
    }

    private void sortTweets(List<Tweet> tweets) {
        Collections.sort(tweets, Comparator.comparing(Tweet::getPosted).reversed());
    }

}
