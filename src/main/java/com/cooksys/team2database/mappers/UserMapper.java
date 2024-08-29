package com.cooksys.team2database.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.team2database.dtos.UserRequestDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.User;

@Mapper(componentModel = "spring", uses = { CredentialsMapper.class, ProfileMapper.class })
public interface UserMapper {
	
	@Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToResponseDto(User user);

    User responseDtoToEntity(UserResponseDto userResponseDto);

    User requestDtoToEntity(UserRequestDto userRequestDto);

    List<UserResponseDto> entityToResponseDtos(List<User> users);

}
