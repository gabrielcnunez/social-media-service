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
        // Remove '#' if present
        String cleanLabel = label.startsWith("#") ? label.substring(1) : label;
        return hashtagRepository.existsByLabel(cleanLabel);
    }

    @Override
    public boolean usernameExists(String username) {
        Optional<User> user = userRepository.findByCredentialsUsername(username);
        return user.isPresent() && !user.get().isDeleted();
    }

    @Override
    public boolean usernameAvailable(String username) {
        Optional<User> user = userRepository.findByCredentialsUsername(username);
        return !user.isPresent() || user.get().isDeleted();
    }
}
