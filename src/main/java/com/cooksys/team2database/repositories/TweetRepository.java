package com.cooksys.team2database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.team2database.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
	
}
