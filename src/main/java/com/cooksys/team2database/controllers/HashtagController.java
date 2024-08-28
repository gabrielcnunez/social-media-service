package com.cooksys.team2database.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	
	private final HashtagService hashtagService;

	// get tags 
	@GetMapping
	public List<String> getAllHashTags(){
		return hashtagService.getAllHashtags();
	}
	
	//get tag by label
	@GetMapping("/{label}")
	public List<TweetResponseDto> getTweetsByHashTag(@PathVariable String label){
		return hashtagService.getTweetsByHashtag(label);
	}
}
