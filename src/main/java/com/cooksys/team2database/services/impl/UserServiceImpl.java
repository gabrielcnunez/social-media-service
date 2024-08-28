package com.cooksys.team2database.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.services.UserService;
import com.cooksys.team2database.repositories.UserRepository;

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

}
