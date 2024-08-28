package com.cooksys.team2database.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.repositories.HashtagRepository;
import com.cooksys.team2database.services.HashtagService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private final HashtagRepository hashtagRepository;
	private final TweetMapper tweetMapper;
	
	@Override
    public List<String> getAllHashtags() {
        // Fetch all hashtags from the repository and extract their labels
        return hashtagRepository.findAll().stream()
                .map(Hashtag::getLabel)
                .collect(Collectors.toList());
    }

	

    @Override
    public List<TweetResponseDto> getTweetsByHashtag(String label) {
        // Find the hashtag by its label
        Hashtag hashtag = hashtagRepository.findByLabel(label);
        if (hashtag == null) {
            throw new NotFoundException("Hashtag with label: " + label + " not found");
        }

        // Get all tweets associated with the hashtag, filter out deleted tweets,
        // sort in reverse chronological order, and collect into a list
        List<Tweet> taggedTweets = hashtag.getTweets().stream()
                .filter(tweet -> !tweet.isDeleted())
                .sorted(Comparator.comparing(Tweet::getPosted).reversed())
                .collect(Collectors.toList());
        // Convert the list of Tweet entities to TweetResponseDto objects
        return tweetMapper.entitiesToDtos(taggedTweets);
    }

}
