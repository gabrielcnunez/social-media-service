package com.cooksys.team2database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.team2database.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	
	// TODO: create custom queries for get commands
	
	// find every tweet that is not deleted
	List<Tweet> findByDeletedFalse();
	
}
