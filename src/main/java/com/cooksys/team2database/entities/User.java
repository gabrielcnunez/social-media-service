package com.cooksys.team2database.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
	  
	  @CreationTimestamp
	  private Timestamp joined;
	  
	  private boolean deleted = false;
	  
	  @Embedded
	  private Credentials credentials;
	  
	  @Embedded 
	  private Profile profile;
	  
//	  One user can have many tweets
	  @OneToMany(mappedBy = "author")
	  private List<Tweet> tweets; 
	  
//	  users can like many tweets
	  @ManyToMany
	  @JoinTable(
			  name = "user_likes",
			  joinColumns = @JoinColumn(name = "user_id"),
			  inverseJoinColumns = @JoinColumn(name = "tweet_id"))
	  private List<Tweet> likedTweets;
	  
//	  users can mention many users in a tweet
	  @ManyToMany(mappedBy = "mentionedUsers")
	   private List<Tweet> mentionedTweets;
	  
//	  a user can follow many users
	  @ManyToMany
	  @JoinTable(name = "followers_following")
	  private List<User> followers;
	  
//	  a user can have many followers
	  @ManyToMany(mappedBy = "followers")
	  private List<User> following;
}
