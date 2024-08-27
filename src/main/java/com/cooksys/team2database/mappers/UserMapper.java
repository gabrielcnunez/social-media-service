package com.cooksys.team2database.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.UserRequestDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.User;

// might use:  uses = { xxxMapper.class }
// tweet might contain user, convert to dto through cascade?
@Mapper(componentModel = "spring", uses = { CredentialsMapper.class, ProfileMapper.class })
public interface UserMapper {
	
	List<UserResponseDto> entityToResponseDtos (List<User> users);
	
	UserResponseDto entityToResponseDto (User user);
	
	User requestDtoToEntity(UserRequestDto userRequestDto);
}
