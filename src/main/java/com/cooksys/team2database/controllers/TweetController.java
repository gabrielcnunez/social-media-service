package com.cooksys.team2database.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetRequestDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
	
	private final TweetService tweetService;

	@GetMapping
	public List<TweetResponseDto> getAllTweets(){
		return tweetService.getAllTweets();
	}
	
	@GetMapping("/{id}")
	public TweetResponseDto getTweetById(@PathVariable Long id) {
		return tweetService.getTweetById(id);
	}
	
	@GetMapping("/{id}/context")
	public ContextDto getTweetContext(@PathVariable Long id) {
		return tweetService.getTweetContext(id);
	}
	
	@GetMapping("/{id}/likes")
	public List<UserResponseDto> getTweetLikes(@PathVariable Long id){
		return tweetService.getTweetLikes(id);
	}
	
	@GetMapping("/{id}/mentions")
	public List<UserResponseDto> getUsersMentionedFromTweet(@PathVariable Long id){
		return tweetService.getUsersMentionedFromTweet(id);
	}
	
	@GetMapping("/{id}/replies")
	public List<TweetResponseDto> getTweetReplies(@PathVariable Long id){
		return tweetService.getTweetReplies(id);
	}
	
	@GetMapping("/{id}/reposts")
	public List<TweetResponseDto> getTweetReposts(@PathVariable Long id){
		return tweetService.getTweetReposts(id);
	}
	
	@GetMapping("/{id}/tags")
	public List<HashtagDto> getTweetHashtags(@PathVariable Long id){
		return tweetService.getTweetHashtags(id);
	}
	@PostMapping("/{id}/like")
	public void likeTweet(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
	    tweetService.likeTweet(id, credentials);
	}
	@DeleteMapping("/{id}")
    public TweetResponseDto deleteTweet(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
        return tweetService.deleteTweet(id, credentials);
    }
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto postTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.postTweet(tweetRequestDto);
	}
	
	@PostMapping("/{id}/reply")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto postReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.postReply(id, tweetRequestDto);
	}
	
	@PostMapping("/{id}/repost")
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResponseDto postRepost(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.postRepost(id, tweetRequestDto);
	}
	
}
