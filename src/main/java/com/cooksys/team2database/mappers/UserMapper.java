package com.cooksys.team2database.mappers;

import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.User;

// might use:  uses = { xxxMapper.class }
// tweet might contain user, convert to dto through cascade?
public interface UserMapper {
	
	// entity to response

	UserResponseDto entityToDto(User entity);
}
