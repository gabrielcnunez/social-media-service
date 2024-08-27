package com.cooksys.team2database.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.TweetRequestDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Tweet;

@Mapper(componentModel = "spring", uses = {  })
public interface TweetMapper {

	// idk if these parameters will work with lombok
	ContextDto contextEntityToResponseDto(Tweet target, List<Tweet> before, List<Tweet> after);
	
	Tweet requestDtoToentity(TweetRequestDto tweetRequestDto);
	
	TweetResponseDto tweetEntityToResponseDto(Tweet tweet);
	
	List<TweetResponseDto> tweetEntityToResponseDtos(List<Tweet> tweets);

}
