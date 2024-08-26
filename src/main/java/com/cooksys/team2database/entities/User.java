package com.cooksys.team2database.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
	
	private String username;
	
	private Profile profile;
	
	private Long joined;
	
	private Boolean deleted = false;
	
	//CHECK: might need credentials object in this entity for username and password 
	@OneToMany
	private List<Tweet> userLikes = new ArrayList<>();
	
	@OneToMany		
	private List<Tweet> timesMentiond = new ArrayList<>();
	
	@OneToMany
	private List<User> followers = new ArrayList<>();
	
	@OneToMany
	private List <User> following = new ArrayList<>();
	

}
