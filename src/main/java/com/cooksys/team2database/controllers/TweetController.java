package com.cooksys.team2database.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.services.TweetService;
import com.cooksys.team2database.entities.Tweet;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private TweetService tweetService;
	
	@GetMapping
	public List<TweetResponseDto> getAllTweets(){
		return tweetService.getAllTweets();
		
	}
	
	

}
