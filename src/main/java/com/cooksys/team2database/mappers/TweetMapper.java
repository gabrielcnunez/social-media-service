package com.cooksys.team2database.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.TweetRequestDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);

    Tweet responseDtoToEntity(TweetResponseDto tweetResponseDto);

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

}
