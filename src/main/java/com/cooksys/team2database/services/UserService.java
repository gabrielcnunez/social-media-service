package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

}
