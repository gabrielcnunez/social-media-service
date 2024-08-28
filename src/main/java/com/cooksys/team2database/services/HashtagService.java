package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.TweetResponseDto;

public interface HashtagService {
	
	List<String> getAllHashtags();
	List<TweetResponseDto> getTweetsByHashtag(String label);
	

}
