package com.cooksys.team2database.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService{
	
	private final TweetRepository tweetRepository;


}
