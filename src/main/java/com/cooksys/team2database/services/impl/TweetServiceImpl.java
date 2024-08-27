package com.cooksys.team2database.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {
	private TweetRepository tweetRepository;
	@Override
	public List<TweetResponseDto> getAllTweets() {
		return tweetRepository.findAll();
	}

}
