package com.cooksys.team2database.entities;

import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Tweet {

	  @Id
	  @GeneratedValue
	  private Long id;
	  
//	  many tweets can belong to one user
	  @ManyToOne
	  @JoinColumn(name = "user_id")
	  private User author;
	  
	  private Long posted;
	  
	  private boolean deleted = false;
	  
	  private String content;
	  
//	  a single tweet can have many replies
	  @ManyToOne
	  private Tweet inReplyTo;
	  
	  @OneToMany(mappedBy = "inReplyTo")
	  private List<Tweet> replies;
	  
//	  a single tweet can have many reposts
	  @ManyToOne
	  private Tweet repostOf;
	  
	  @OneToMany(mappedBy = "repostOf")
	  private List<Tweet> reposts;
	  
//	  a tweet can have many hashtags
	  @ManyToMany
	  @JoinTable(
			  name = "tweet_hashtags",
			  joinColumns = @JoinColumn(name = "tweet_id"),
			  inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	  private List<Hashtag> hashtagsInTweet;
	  
//	  a tweet can have many likes
	  @ManyToMany(mappedBy = "likesFromUser")
	  private List<User> usersFromLike;
	  
//	  tweets can mention many users
	  @ManyToMany(mappedBy = "tweetsMentioningUser")
	  private List<User> usersMentionedInTweet;

}
