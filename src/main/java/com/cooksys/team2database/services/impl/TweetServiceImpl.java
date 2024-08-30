package com.cooksys.team2database.services.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.dtos.ContextDto;
import com.cooksys.team2database.dtos.CredentialsDto;
import com.cooksys.team2database.dtos.HashtagDto;
import com.cooksys.team2database.dtos.TweetResponseDto;
import com.cooksys.team2database.dtos.UserResponseDto;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.exceptions.BadRequestException;
import com.cooksys.team2database.exceptions.NotFoundException;
import com.cooksys.team2database.mappers.CredentialsMapper;
import com.cooksys.team2database.mappers.HashtagMapper;
import com.cooksys.team2database.mappers.TweetMapper;
import com.cooksys.team2database.mappers.UserMapper;
import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.repositories.UserRepository;
import com.cooksys.team2database.services.TweetService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

// TODO: look over and turn reused code into functions. Rn the delete filter using 
// iterator with a while loop comes to mind for both a list of users and list of tweets.

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {

	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;
	private final TweetMapper tweetMapper;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	private final CredentialsMapper credentialsMapper;


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

		// get reply chain before current tweet, excluding deleted tweets
		while (currTweet != null) {
			if (!currTweet.isDeleted()) {
				beforeTweets.add(currTweet);
			}
			currTweet = currTweet.getInReplyTo();
		}

		// get all replies of current tweet
		List<Tweet> afterTweets = tweetRepository.getReferenceById(id).getReplies();
		getActiveTweets(afterTweets);
		// get the replies of replies of current tweet
		// This was my interpretation of the instructions for this part. Not having an
		// example output makes this a little hard to figure out
		for (int i = 0; i < afterTweets.size(); i++) {
			for (int j = 0; j < afterTweets.get(i).getReplies().size(); j++) {
				if (!afterTweets.get(i).getReplies().get(j).isDeleted()) {
					afterTweets.add(afterTweets.get(i).getReplies().get(j));
				}
			}
		}
		// chronological order
		afterTweets.sort(Comparator.comparing(Tweet::getPosted));

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

		return userMapper.entityToResponseDtos(likedByUsers);
	}

	// TODO: IMPORTANT: when a tweet with content is created, the server must
	// process the tweet's content for @{username} mentions and #{hashtag} tags.
	// There is no way to create hashtags or create mentions from the API, so this
	// must be handled automatically!
	
	// I'm guessing this is going to be using regex and will read through the
	// content of a tweet during a post request.
	@Override
	public List<UserResponseDto> getUsersMentionedFromTweet(Long id) {
		validateTweetId(id);
		List<User> mentionsInTweet = tweetRepository.getReferenceById(id).getMentionedUsers();
		getActiveUsers(mentionsInTweet);

		return userMapper.entityToResponseDtos(mentionsInTweet);
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
	
	@Transactional
	@Override
	public void likeTweet(Long id, CredentialsDto credentials) {
	    // Find the tweet by id
	    Tweet tweet = tweetRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("No tweet found with id: " + id));

	    // Check if the tweet has been deleted
	    if (tweet.isDeleted()) {
	        throw new NotFoundException("The tweet with id: " + id + " has been deleted");
	    }

	    // Find the user by username
	    User user = userRepository.findByCredentialsUsername(credentials.getUsername())
	            .orElseThrow(() -> new BadRequestException("Invalid credentials"));

	    // Verify user's password
	    if (!user.getCredentials().getPassword().equals(credentials.getPassword())) {
	        throw new BadRequestException("Invalid credentials");
	    }

	    // Check if the user has already liked this tweet
	    if (tweet.getLikedByUsers().contains(user)) {
	        throw new BadRequestException("User has already liked this tweet");
	    }

	    // Add the user to the tweet's liked users and vice versa
	    tweet.getLikedByUsers().add(user);
	    user.getLikedTweets().add(tweet);

	    // Save the updated tweet and user
	    tweetRepository.save(tweet);
	    userRepository.save(user);
	}

	@Transactional
	@Override
	public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
	    // Find the tweet by id
	    Tweet tweet = tweetRepository.findById(id)
	            .orElseThrow(() -> new NotFoundException("No tweet found with id: " + id));

	    // Find the user by username
	    User user = userRepository.findByCredentialsUsername(credentialsDto.getUsername())
	            .orElseThrow(() -> new BadRequestException("User not found"));

	    // Verify user's password
	    if (!user.getCredentials().getPassword().equals(credentialsDto.getPassword())) {
	        throw new BadRequestException("Invalid credentials");
	    }

	    // Check if the user is the author of the tweet
	    if (!tweet.getAuthor().equals(user)) {
	        throw new BadRequestException("User is not the author of this tweet");
	    }

	    // Check if the tweet is already deleted
	    if (tweet.isDeleted()) {
	        throw new BadRequestException("Tweet is already deleted");
	    }

	    // Mark the tweet as deleted
	    tweet.setDeleted(true);

	    // Save and flush the changes to the database
	    Tweet savedTweet = tweetRepository.saveAndFlush(tweet);

	    // Convert the saved tweet to a DTO and return it
	    return tweetMapper.entityToDto(savedTweet);
	}

}
