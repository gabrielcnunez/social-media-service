package com.cooksys.team2database.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.team2database.repositories.HashtagRepository;
import com.cooksys.team2database.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private final HashtagRepository hashtagRepository;

}
