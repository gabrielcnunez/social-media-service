package com.cooksys.team2database.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.repositories.HashtagRepository;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;

    //Checks if a hashtag exists in the database
    
    @Override
    public boolean hashtagExists(String label) {
        return hashtagRepository.existsByLabel(label);
    }
    
    //Checks if a username exists in the database
    //This method checks for the existence of the username, requardless of whether the user is deleted or not

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByCredentialsUsername(username);
    }
    
    @Override
	public boolean usernameAvailable(String username) {
		if (!usernameExists(username)) {
			return true;
		}
		Optional<User> optionalDeletedUser = userRepository.findByCredentialsUsername(username);
		if (optionalDeletedUser.get().isDeleted()) {
			return true;
		}
		return false;
	}
}
