package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

	ContextDto getTweetContext(Long id);

	List<UserResponseDto> getTweetLikes(Long id);

	List<UserResponseDto> getUsersMentionedFromTweet(Long id);

}
