package com.cooksys.team2database.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	List<HashtagDto> entityToDtos(List<Hashtag> hashtags);
	
}
