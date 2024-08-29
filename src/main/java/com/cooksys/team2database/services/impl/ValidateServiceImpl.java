package com.cooksys.team2database.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.entities.Credentials;
import com.cooksys.team2database.entities.Profile;
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

    //Checks f a username is available for use
    //A username is considered available if it does not exist in the database
    //or if it belongs to a deleted user
    
     // TODO: don't use custom repository function name to check all usernames
    // List<User> allUsers = getAllUsers()
    // loop through allUsers
    // get all their usernames
    // compare to username passed into the function
    // if you get any match, return false
    // else return true
    
    @Override
    public boolean usernameAvailable(String username) {
    	Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    	Profile tempProfile = new Profile();
    	tempProfile.setEmail("aaaaaaaaaaaaaaa");
    	Credentials tempCredential = new Credentials();
    	tempCredential.setPassword("aaaaaaaaaa");
    	tempCredential.setUsername("aaaaaaaaaaaaaa");
    	optionalUser.get().setCredentials(tempCredential);
    	optionalUser.get().setProfile(tempProfile);
        return optionalUser.isEmpty() || optionalUser.get().isDeleted();
    }
}
