package com.cooksys.team2database.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.HashtagMapper;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.repositories.HashtagRepository;
import com.cooksys.team2database.services.HashtagService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private final HashtagRepository hashtagRepository;
	private final TweetMapper tweetMapper;
	private final HashtagMapper hashtagMapper;
	
	@Override
    public List<HashtagDto> getAllHashtags() {
        // Fetch all hashtags from the repository and extract their labels
        return hashtagRepository.findAll().stream()
                .map(hashtagMapper::entityToDto)
                .collect(Collectors.toList());
    }

	

	@Override
    public List<TweetResponseDto> getTweetsByHashtag(String label) {
		// Retrieve the hashtag by its label
        Hashtag hashtag = hashtagRepository.findByLabel(label);
        if (hashtag == null) {
            throw new NotFoundException("Hashtag with label: " + label + " not found");
        }
        // Filter and sort the tweets associated with the hashtag that are not deleted and contain the hashtag label

        List<Tweet> taggedTweets = hashtag.getTweets().stream()
                .filter(tweet -> !tweet.isDeleted() && tweet.getContent() != null && tweet.getContent().contains("#" + label))
                .sorted((t1, t2) -> t2.getPosted().compareTo(t1.getPosted()))
                .collect(Collectors.toList());

        return tweetMapper.entitiesToDtos(taggedTweets);
    }

    @Override
    public Hashtag getOrCreateHashtag(String label) {
        Hashtag hashtag = hashtagRepository.findByLabel(label);
        if (hashtag == null) {
            hashtag = new Hashtag();
            hashtag.setLabel(label);
            //set the first used timestamp to the current time
            hashtag.setFirstUsed(new Timestamp(System.currentTimeMillis()));
        }
        //update the last used timestamp to the current time
        hashtag.setLastUsed(new Timestamp(System.currentTimeMillis()));
        return hashtagRepository.save(hashtag);
    }

    @Override
    public void updateHashtagUsage(Hashtag hashtag) {
        // Update the last used timestamp of the hashtag to the current time
        hashtag.setLastUsed(new Timestamp(System.currentTimeMillis()));
        hashtagRepository.save(hashtag);
    }
}
