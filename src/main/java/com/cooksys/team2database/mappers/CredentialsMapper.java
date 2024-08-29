package com.cooksys.team2database.mappers;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.entities.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
	
	CredentialsDto entityToDto(Credentials credentials);

    Credentials dtoToEntity(CredentialsDto dto);
	
}
