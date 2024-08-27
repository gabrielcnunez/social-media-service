package com.cooksys.team2database.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private final TweetService tweetService;

}
