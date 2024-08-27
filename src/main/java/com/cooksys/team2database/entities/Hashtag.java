package com.cooksys.team2database.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
	
	  @Id
	  @GeneratedValue
	  private Long id;
	  
	  private String label;
	  
	  private Long firstUsed;
	  
	  private Long lastUsed;
	  
	  @ManyToMany(mappedBy = "hashtagsInTweet")
	  private List<Tweet> tweetsInHashtag;

	  
}
