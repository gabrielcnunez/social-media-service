package com.cooksys.team2database.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	
	//database is supposed to have this type as 'timestamp'
	private Long firstUse;
	
	private Long lastUse;
	
	@OneToMany
	private List<Tweet> tweetsUsingThisHashtag = new ArrayList<>();	

}
