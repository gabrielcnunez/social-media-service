package com.cooksys.team2database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.team2database.entities.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
	Hashtag findByLabel(String label); //finds hashtag by its label
	
	boolean existsByLabel(String label); // checks if a hashtag with the given label exists

}

