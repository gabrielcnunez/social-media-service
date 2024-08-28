package com.cooksys.team2database.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;

// TODO: look over and turn reused code into functions. Rn the delete filter using iterator with a while loop comes to mind.

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;

	private void validateTweetId(Long id) {
		if (!tweetRepository.existsById(id) || tweetRepository.getReferenceById(id).isDeleted()) {
			throw new NotFoundException("Id: " + id + " is deleted or does not exist!");
		}
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> notDeletedTweets = new ArrayList<Tweet>();

		for (Tweet tweet : tweetRepository.findAll()) {
			// only add tweets that are not deleted
			if (!tweet.isDeleted()) {
				notDeletedTweets.add(tweet);
			}
		}
		// reverse chronological order
		notDeletedTweets.sort(Comparator.comparing(Tweet::getPosted).reversed());
		return tweetMapper.entitiesToDtos(notDeletedTweets);
	}

	@Override
	public TweetResponseDto getTweetById(Long id) {
		validateTweetId(id);
		return tweetMapper.entityToDto(tweetRepository.getReferenceById(id));
	}

	// TODO: IMPORTANT: While deleted tweets should not be included in the before
	// and after properties of the result, transitive replies should. What that
	// means is that if a reply to the target of the context is deleted, but there's
	// another reply to the deleted reply, the deleted reply should be excluded but
	// the other reply should remain.
	@Override
	public ContextDto getTweetContext(Long id) {
		validateTweetId(id);
		Tweet targetTweet = tweetRepository.getReferenceById(id);
		Tweet currTweet = targetTweet.getInReplyTo();
		List<Tweet> beforeTweets = new ArrayList<Tweet>();

		while (currTweet != null) {
			// only add tweets that are not deleted to previous tweets list
			if (!currTweet.isDeleted()) {
				beforeTweets.add(currTweet);
			}
			currTweet = currTweet.getInReplyTo();
		}

		List<Tweet> afterTweets = targetTweet.getReplies();
		// only add tweets that are not deleted to replies list
		Iterator<Tweet> iterator = afterTweets.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().isDeleted()) {
				iterator.remove();
			}
		}

		ContextDto contextDto = new ContextDto();
		contextDto.setTarget(tweetMapper.entityToDto(targetTweet));
		contextDto.setBefore(tweetMapper.entitiesToDtos(beforeTweets));
		contextDto.setAfter(tweetMapper.entitiesToDtos(afterTweets));

		return contextDto;
	}

	@Override
	public List<UserResponseDto> getTweetLikes(Long id) {
		validateTweetId(id);
		Tweet targetTweet = tweetRepository.getReferenceById(id);
		List<User> likedByUsers = targetTweet.getLikedByUsers();
		// only add users that are not deleted
		Iterator<User> iterator = likedByUsers.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().isDeleted()) {
				iterator.remove();
			}
		}
		return userMapper.entitiesToDtos(likedByUsers);
	}

	// TODO: IMPORTANT: when a tweet with content is created, the server must
	// process the tweet's content for @{username} mentions and #{hashtag} tags.
	// There is no way to create hashtags or create mentions from the API, so this
	// must be handled automatically!
	@Override
	public List<UserResponseDto> getUsersMentionedFromTweet(Long id) {
		validateTweetId(id);
		Tweet targetTweet = tweetRepository.getReferenceById(id);
		List<User> mentionsInTweet = targetTweet.getMentionedUsers();
		// only add users that are not deleted
		Iterator<User> iterator = mentionsInTweet.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().isDeleted()) {
				iterator.remove();
			}
		}
		return userMapper.entitiesToDtos(mentionsInTweet);
	}

}
