package com.cooksys.team2database.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	
	@ManyToOne
	private User author;
	
	private Long posted;
	
	private Boolean deleted = false;
	
	private String content;
	
	private Tweet  inReplyTo;
	
	private Tweet  repostOf;
	
	@OneToMany
	private List <User> tweetLikes = new ArrayList<>();
	
	@OneToMany 
	private List<Hashtag> hashtagsInTweet = new ArrayList<>();
	//possibly needs mappedBy
	
	@OneToMany
	private List<User> tweetMentions = new ArrayList<>();


}
