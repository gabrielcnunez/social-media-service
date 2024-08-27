package com.cooksys.team2database.repositories;

import java.util.List;

import com.cooksys.team2database.dtos.TweetResponseDto;

public interface TweetRepository {

	List<TweetResponseDto> findAll();

}
