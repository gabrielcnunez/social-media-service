package com.cooksys.team2database.mappers;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.ProfileDto;
import com.cooksys.team2database.entities.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
	
	ProfileDto entityToDto(Profile entity);

    Profile requestDtoToEntity(ProfileDto profileDto);

}
