package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.TweetResponseDto;


public interface TweetService {

	List<TweetResponseDto> getAllTweets();


}
