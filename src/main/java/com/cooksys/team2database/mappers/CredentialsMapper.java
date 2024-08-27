package com.cooksys.team2database.mappers;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	Credentials requestDtoToEntity(CredentialsDto credentialsDto);
	
}
