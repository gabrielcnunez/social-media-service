package com.cooksys.team2database.services;

import java.util.List;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetRequestDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;

public interface TweetService {

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id);

	ContextDto getTweetContext(Long id);

	List<UserResponseDto> getTweetLikes(Long id);

	List<UserResponseDto> getUsersMentionedFromTweet(Long id);

	List<TweetResponseDto> getTweetReplies(Long id);

	List<TweetResponseDto> getTweetReposts(Long id);

	List<HashtagDto> getTweetHashtags(Long id);
	
	void likeTweet(Long id, CredentialsDto credentials);
	
	TweetResponseDto deleteTweet(Long id, CredentialsDto credentials);

	TweetResponseDto postTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto postReply(Long id, TweetRequestDto tweetRequestDto);

	TweetResponseDto postRepost(Long id, TweetRequestDto tweetRequestDto);

}
