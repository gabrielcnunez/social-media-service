package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Hashtag;

public interface HashtagService {
	
	List<HashtagDto> getAllHashtags();
	List<TweetResponseDto> getTweetsByHashtag(String label);
	Hashtag getOrCreateHashtag(String label);
    void updateHashtagUsage(Hashtag hashtag);

}
