package com.cooksys.team2database.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.HashtagMapper;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.services.TweetService;

import lombok.AllArgsConstructor;

// TODO: look over and turn reused code into functions. Rn the delete filter using 
// iterator with a while loop comes to mind for both a list of users and list of tweets.

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;

	// check if tweet with given id is deleted or does not exist in database
	private void validateTweetId(Long id) {
		if (!tweetRepository.existsById(id) || tweetRepository.getReferenceById(id).isDeleted()) {
			throw new NotFoundException("Id: " + id + " is deleted or does not exist!");
		}
	}

	// only add tweets that are not deleted
	private void getActiveTweets(List<Tweet> tweets) {
		Iterator<Tweet> iterator = tweets.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().isDeleted()) {
				iterator.remove();
			}
		}
	}

	// only add users that are not deleted
	private void getActiveUsers(List<User> users) {
		Iterator<User> iterator = users.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().isDeleted()) {
				iterator.remove();
			}
		}
	}

	@Override
	public List<TweetResponseDto> getAllTweets() {
		List<Tweet> notDeletedTweets = tweetRepository.findByDeletedFalse();

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

	// The chains should be in chronological order, and the after chain should
	// include all replies of replies, meaning that all branches of replies must be
	// flattened into a single chronological list to fully satisfy the requirements.
	@Override
	public ContextDto getTweetContext(Long id) {
		validateTweetId(id);
		Tweet currTweet = tweetRepository.getReferenceById(id).getInReplyTo();
		List<Tweet> beforeTweets = new ArrayList<Tweet>();

		// get reply chain before current tweet
		while (currTweet != null) {
			beforeTweets.add(currTweet);
			currTweet = currTweet.getInReplyTo();
		}

		List<Tweet> afterTweets = tweetRepository.getReferenceById(id).getReplies();
//		getActiveTweets(afterTweets);

		ContextDto contextDto = new ContextDto();
		contextDto.setTarget(tweetMapper.entityToDto(tweetRepository.getReferenceById(id)));
		contextDto.setBefore(tweetMapper.entitiesToDtos(beforeTweets));
		contextDto.setAfter(tweetMapper.entitiesToDtos(afterTweets));

		return contextDto;
	}

	@Override
	public List<UserResponseDto> getTweetLikes(Long id) {
		validateTweetId(id);
		List<User> likedByUsers = tweetRepository.getReferenceById(id).getLikedByUsers();
		getActiveUsers(likedByUsers);

		return userMapper.entitiesToDtos(likedByUsers);
	}

	// TODO: IMPORTANT: when a tweet with content is created, the server must
	// process the tweet's content for @{username} mentions and #{hashtag} tags.
	// There is no way to create hashtags or create mentions from the API, so this
	// must be handled automatically!
	@Override
	public List<UserResponseDto> getUsersMentionedFromTweet(Long id) {
		validateTweetId(id);
		List<User> mentionsInTweet = tweetRepository.getReferenceById(id).getMentionedUsers();
		getActiveUsers(mentionsInTweet);

		return userMapper.entitiesToDtos(mentionsInTweet);
	}

	@Override
	public List<TweetResponseDto> getTweetReplies(Long id) {
		validateTweetId(id);
		List<Tweet> tweetReplies = tweetRepository.getReferenceById(id).getReplies();
		getActiveTweets(tweetReplies);

		return tweetMapper.entitiesToDtos(tweetReplies);
	}

	@Override
	public List<TweetResponseDto> getTweetReposts(Long id) {
		validateTweetId(id);
		List<Tweet> tweetReposts = tweetRepository.getReferenceById(id).getReposts();
		System.out.println("here");
		getActiveTweets(tweetReposts);

		return tweetMapper.entitiesToDtos(tweetReposts);
	}

	@Override
	public List<HashtagDto> getTweetHashtags(Long id) {
		validateTweetId(id);
		List<Hashtag> tweetHashtags = tweetRepository.getReferenceById(id).getHashtags();

		return hashtagMapper.entitiesToDtos(tweetHashtags);
	}

}
