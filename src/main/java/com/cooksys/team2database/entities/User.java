package com.cooksys.team2database.entities;

import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_table")
@Entity
@NoArgsConstructor
@Data
public class User {

	  @Id
	  @GeneratedValue
	  private Long id;
	  
	  private Long joined;
	  
	  private boolean deleted = false;
	  
	  @Embedded
	  Credentials credentials;
	  
	  @Embedded 
	  Profile profile;
	  
//	  One user can have many tweets
	  @OneToMany(mappedBy = "author")
	  private List<Tweet> tweets; 
	  
//	  users can like many tweets
	  @ManyToMany
	  @JoinTable(
			  name = "user_likes",
			  joinColumns = @JoinColumn(name = "user_id"),
			  inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	  private List<Tweet> likesFromUser;
	  
//	  users can mention many users in a tweet
	  @ManyToMany
	  @JoinTable(
			  name = "user_mentions",
			  joinColumns = @JoinColumn(name = "user_id"),
			  inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	  private List<Tweet> tweetsMentioningUser;
	  
//	  a user can follow many users
	  @ManyToMany
	  @JoinTable(
			  name = "followers_following",
			  joinColumns = @JoinColumn(name = "follower_id"),
			  inverseJoinColumns = @JoinColumn(name = "following_id"))
	  private List<User> followingList;
	  
//	  a user can have many followers
	  @ManyToMany(mappedBy = "followingList")
	  private List<User> followersList;
}
