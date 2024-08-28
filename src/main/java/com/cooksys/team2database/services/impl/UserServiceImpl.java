package com.cooksys.team2database.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
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
	
	private User getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("No user found with username: @" + username);
		}
		
		return optionalUser.get();
	}

}
