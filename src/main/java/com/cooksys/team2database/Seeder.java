package com.cooksys.team2database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.team2database.entities.Credentials;
import com.cooksys.team2database.entities.Hashtag;
import com.cooksys.team2database.entities.Profile;
import com.cooksys.team2database.entities.Tweet;
import com.cooksys.team2database.entities.User;
import com.cooksys.team2database.repositories.HashtagRepository;
import com.cooksys.team2database.repositories.TweetRepository;
import com.cooksys.team2database.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

	private final HashtagRepository hashtagRepository;
	private final TweetRepository tweetRepository;
	private final UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {

//		create user 1
		User user1 = new User();
		Date date = new Date();
		Credentials credentials1 = new Credentials();
		Profile profile1 = new Profile();
		long joined = date.getTime();

		credentials1.setUsername("user1");
		credentials1.setPassword("1234");

		profile1.setEmail("user1@gmail.com");
		profile1.setFirstName("user");
		profile1.setLastName("one");
		profile1.setPhone("1234567890");

		user1.setJoined(joined);
		user1.setCredentials(credentials1);
		user1.setProfile(profile1);

//		create tweet 1
		Tweet tweet1 = new Tweet();
		long posted = date.getTime();

		tweet1.setAuthor(user1);
		tweet1.setPosted(posted);
		tweet1.setContent("simple tweet, user1's tweet1");

//		create hashtag 1
		Hashtag hashtag1 = new Hashtag();
		List<Tweet> tweetsUsingHashtag1 = new ArrayList<>();
		tweetsUsingHashtag1.add(tweet1);
		hashtag1.setLabel("hashtag1");
		hashtag1.setFirstUsed(posted);
		hashtag1.setTweetsInHashtag(tweetsUsingHashtag1);

//		attach hashtag 1 to tweet 1
		List<Hashtag> hashtagsInTweet1 = new ArrayList<Hashtag>();
		hashtagsInTweet1.add(hashtag1);
		tweet1.setHashtagsInTweet(hashtagsInTweet1);

//		add tweets list to user
		List<Tweet> tweetsFromUser = new ArrayList<Tweet>();
		tweetsFromUser.add(tweet1);
		user1.setTweets(tweetsFromUser);

//		create user 2
		User user2 = new User();
		Credentials credentials2 = new Credentials();
		Profile profile2 = new Profile();
		joined = date.getTime();

		credentials2.setUsername("user2");
		credentials2.setPassword("5678");

		profile2.setEmail("user2@hotmail.com");
		profile2.setFirstName("yooser");
		profile2.setLastName("two");
		profile2.setPhone("0987654321");

		user2.setJoined(joined);
		user2.setCredentials(credentials2);
		user2.setProfile(profile2);
		
//		create tweet 2
		Tweet tweet2 = new Tweet();
		long posted2 = date.getTime();

		tweet2.setAuthor(user2);
		tweet2.setPosted(posted2);
		tweet2.setContent("simple tweet, user2's tweet1");
		
//		add tweets list to user
		List<Tweet> tweetsFromUser2 = new ArrayList<Tweet>();
		tweetsFromUser.add(tweet2);
		user2.setTweets(tweetsFromUser2);		

//		user 2 follows user 1, update follow and followers list 
//		for both users
		List<User> userFollowingList = new ArrayList<User>();
		userFollowingList.add(user1);
		user2.setFollowingList(userFollowingList);

		List<User> userFollowersList = new ArrayList<User>();
		userFollowersList.add(user2);
		user1.setFollowersList(userFollowersList);

//		user 2 likes user 1's tweet, update the respective lists
		List<Tweet> userLikes = new ArrayList<Tweet>();
		userLikes.add(tweet1);
		user2.setLikesFromUser(userLikes);

		List<User> usersThatLiked = new ArrayList<User>();
		usersThatLiked.add(user2);
		tweet1.setUsersFromLike(userFollowersList);

		userRepository.saveAndFlush(user1);

		tweetRepository.saveAndFlush(tweet1);

		hashtagRepository.saveAndFlush(hashtag1);

		userRepository.saveAndFlush(user2);
		
		tweetRepository.saveAndFlush(tweet2);

	}

}
