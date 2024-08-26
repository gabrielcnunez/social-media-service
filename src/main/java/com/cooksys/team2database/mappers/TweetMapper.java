package com.cooksys.team2database.mappers;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Tweet;

public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet entity);

	// omitting request to entity, can't find any endpoint that gives us a tweet
	// request

}
